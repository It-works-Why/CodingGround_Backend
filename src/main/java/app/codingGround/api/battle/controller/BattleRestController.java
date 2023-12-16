package app.codingGround.api.battle.controller;

import app.codingGround.api.battle.dto.request.CodeData;
import app.codingGround.api.battle.dto.request.ConnectGameInfo;
import app.codingGround.api.battle.dto.request.Message;
import app.codingGround.api.battle.dto.response.*;
import app.codingGround.api.battle.service.BattleService;
import app.codingGround.api.entity.Language;
import app.codingGround.domain.common.dto.response.DefaultResultDto;
import app.codingGround.global.config.exception.CustomException;
import app.codingGround.global.config.exception.ErrorCode;
import app.codingGround.global.config.model.ApiResponse;
import app.codingGround.global.utils.JwtTokenProvider;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/battle")
public class BattleRestController {

    private final BattleService battleService;
    private final RabbitTemplate rabbitTemplate;
    private final SimpMessageSendingOperations messagingTemplate;


    @Value("${spring.redis.host}")
    @Getter
    private String redisHost;

    @Value("${spring.redis.port}")
    @Getter
    private int redisPort;

    private Jedis getJedisInstance() {
        Jedis jedis = new Jedis(getRedisHost(), getRedisPort());
        return jedis;
    }

    private void closeJedisInstance(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }

    @GetMapping("/get/language")
    public List<Language> getLanguage() {
        return battleService.getLanguage();
    }

    @PostMapping("/join/game")
    public ResponseEntity<ApiResponse<QueueInfoDto>> tryGameConnect(@RequestHeader("Authorization") String accessToken, @RequestBody ConnectGameInfo connectGameInfo) {
        // 게임방 생성 또는 빈 방 확인 로직
        Message message = new Message();
        Jedis jedis = null;
        String lockKey = null;
        QueueInfoDto queueInfoDto = null;
        try {
            jedis = getJedisInstance();
            lockKey = "connectionLock";
            int maxRetries = 7; // 최대 재시도 횟수
            int waitTimeMs = 3000; // 재시도 간격 (5초)
            String lockResult = null;
            int retries = 0;
            while (lockResult == null && retries < maxRetries) {
                lockResult = jedis.set(lockKey, "LOCKED", SetParams.setParams().nx().ex(10)); // 락의 만료 시간을 설정합니다. (예: 10초)
                if (lockResult == null) {
                    retries++;
                    try {
                        Thread.sleep(waitTimeMs); // 일정 시간 대기 후 재시도
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
            if (lockResult != null) {
                queueInfoDto = battleService.tryGameConnect(connectGameInfo, accessToken);
                if(queueInfoDto.getConnectType().equals("failed") && queueInfoDto.getGameId() != null){

                    battleService.escapeGame(JwtTokenProvider.getUserId(accessToken));
                    battleService.denyReconnect(accessToken);
                    message.setUrl("/topic/public/disconnect/user/"+queueInfoDto.getGameId()+"/"+ JwtTokenProvider.getUserId(accessToken));
                    message.setData(queueInfoDto);
                    message.setType("disconnect");
                    message.setGameId(queueInfoDto.getGameId());
                    rabbitTemplate.convertAndSend("game.exchange", "*.room.1", message);
                }
            }
        } finally {
            jedis.del(lockKey);
            closeJedisInstance(jedis);
        }
        if (queueInfoDto != null) {
            return ResponseEntity.ok(new ApiResponse<>(queueInfoDto));
        } else {
            throw new CustomException("게임방 생성 실패", ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/reconnect/game")
    public ResponseEntity<ApiResponse<DefaultResultDto>> tryGameReconnect(@RequestHeader("Authorization") String accessToken) {
        return ResponseEntity.ok(new ApiResponse<>(battleService.acceptReconnect(accessToken)));
    }

    @PostMapping("/disconnect/game")
    public ResponseEntity<ApiResponse<DefaultResultDto>> tryGameDisconnect(@RequestHeader("Authorization") String accessToken) {
        return ResponseEntity.ok(new ApiResponse<>(battleService.denyReconnect(accessToken)));
    }

    @GetMapping("/get/question/{gameId}")
    public ResponseEntity<ApiResponse<BattleData>> getQuestion(@PathVariable String gameId) {
        // gameId 의 현재 라운드수, gameNum 을 redis에서 조회 후 RDS에서 조회후 리턴
        BattleData battleData = new BattleData();
        QuestionDto questionDto = battleService.getQuestion(gameId);
        List<TestCaseDto> testCase = battleService.getTestcase(gameId);
        battleData.setQuestionDto(questionDto);
        battleData.setTestCase(testCase);

        return ResponseEntity.ok(new ApiResponse<>(battleData));
    }


    @GetMapping("/check/{gameId}")
    public ResponseEntity<ApiResponse<DefaultResultDto>> userCheck(@PathVariable String gameId, @RequestHeader("Authorization") String accessToken) {
        String userId = JwtTokenProvider.getUserId(accessToken);
        String failed = battleService.authJoinUser(gameId, userId);
        if (failed.equals("failed")) {
            return ResponseEntity.ok(new ApiResponse<>(DefaultResultDto.builder().message("잘못된 접근입니다.").success(false).build()));
        } else {
            refreshUserList(gameId);
            return ResponseEntity.ok(new ApiResponse<>(DefaultResultDto.builder().message("허용").success(true).build()));
        }
    }

    @PostMapping("/send/1/{gameId}")
    public ResponseEntity<ApiResponse<List<TestCaseResultDto>>> sendCodeRound1(@PathVariable String gameId, @RequestBody CodeData codeData) {
        ResultDto resultDto = battleService.runCode(codeData, gameId, 1);
        List<TestCaseResultDto> testCaseResultDtos = resultDto.getTestCaseResultDtos();

        refreshUserList(gameId);

        return  ResponseEntity.ok(new ApiResponse<>(testCaseResultDtos));
    }
    @PostMapping("/send/2/{gameId}")
    public ResponseEntity<ApiResponse<List<TestCaseResultDto>>> sendCodeRound2(@PathVariable String gameId, @RequestBody CodeData codeData) {
        ResultDto resultDto = battleService.runCode(codeData, gameId, 2);
        List<TestCaseResultDto> testCaseResultDtos = resultDto.getTestCaseResultDtos();

        refreshUserList(gameId);

        return  ResponseEntity.ok(new ApiResponse<>(testCaseResultDtos));
    }




    public void refreshUserList(String gameId) {
        List<GameUserDto> gamePlayers = battleService.getGameUserDtoList(gameId);
        Message message = new Message();
        message.setType("refresh");
        message.setUrl("/topic/public/refresh/user/" + gameId);
        message.setData(gamePlayers);
        message.setGameId(gameId);
        rabbitTemplate.convertAndSend("game.exchange", "*.room."+gameId, message);

    }
}

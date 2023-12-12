package app.codingGround.api.battle.controller;


import app.codingGround.api.battle.dto.request.CodeData;
import app.codingGround.api.battle.dto.response.*;
import app.codingGround.api.battle.service.BattleService;
import app.codingGround.api.entity.Question;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Controller
@RequiredArgsConstructor
public class BattleController {

    private final BattleService battleService;
    private final SimpMessageSendingOperations messagingTemplate;

    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private int redisPort;


    public String getRedisHost() {
        return redisHost;
    }

    public int getRedisPort() {
        return redisPort;
    }

    private Jedis getJedisInstance() {
        Jedis jedis = new Jedis(getRedisHost(), getRedisPort());
        return jedis;
    }

    private void closeJedisInstance(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }

    @MessageMapping("/join/queue/{gameId}")
    public void sendMessage(@DestinationVariable String gameId, @Payload String userId, SimpMessageHeaderAccessor headerAccessor) {
        PlayUserInfo playUserInfo = new PlayUserInfo();
        if (userId != null) {
            headerAccessor.getSessionAttributes().put("userId", userId);
        }
        String failed = battleService.authJoinUser(gameId, userId);
        List<GameUserDto> gameUserDtoList = battleService.getGameUserDtoList(gameId);
        long userCount = gameUserDtoList.size();
        playUserInfo.setPlayUsers(gameUserDtoList);
        playUserInfo.setUserTotalCount(userCount);

        if (failed.equals("failed")) {
            messagingTemplate.convertAndSend("/topic/public/getGameUsersData/" + failed + "/" + gameId + "/" + userId, playUserInfo);
        } else {
            messagingTemplate.convertAndSend("/topic/public/getGameUsersData/" + failed + "/" + gameId, playUserInfo);
        }
//      게임 타입이 WAIT 이고, 유저 인원수가 8명일때! 게임시작 전송
        String gameStatus = battleService.getGameStatus(gameId);
        Jedis jedis = null;
        String lockKey = "";
        jedis = getJedisInstance();
        lockKey = "game_room_lock:" + gameId;
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
                    // 스레드 인터럽트 발생 시 처리
                }
            }
        }
        if (lockResult != null && gameStatus.equals("WAIT") && userCount == 8) { // 테스트를 위해 2로 해놓음
            jedis.del(lockKey);
            closeJedisInstance(jedis);
            battleService.startGame(gameId);
            LocalDateTime currentTime = LocalDateTime.now();
            LocalDateTime futureTime = currentTime.plusSeconds(10);
            List<Question> questions = battleService.startRound(gameId);

            messagingTemplate.convertAndSend("/topic/public/gameStart/" + gameId, futureTime);
            ScheduledExecutorService scheduler1 = Executors.newScheduledThreadPool(1);
            int round1LimitMinutes = questions.get(0).getQuestionLimitTime();
            long delayInSeconds1 = round1LimitMinutes * 60L + 5L; // 현재 분을 초로 환산 후 5초를 추가합니다.
            Runnable eventTask1 = () -> {
                System.out.println("1라운드 끝!");
                battleService.endRound1(gameId);
                messagingTemplate.convertAndSend("/topic/public/round1/end/front/" + gameId, gameId);
                try {
                    Thread.sleep(5000);
                    battleService.addRound1Record(gameId);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            };
            scheduler1.schedule(eventTask1, delayInSeconds1, TimeUnit.SECONDS);

            ScheduledExecutorService scheduler2 = Executors.newScheduledThreadPool(1);
            int round2LimitMinutes = questions.get(1).getQuestionLimitTime() + questions.get(0).getQuestionLimitTime();
            long delayInSeconds2 = round2LimitMinutes * 60L + 5L; // 현재 분을 초로 환산 후 5초를 추가합니다.
            Runnable eventTask2 = () -> {
                messagingTemplate.convertAndSend("/topic/public/round2/end/front/" + gameId, gameId);
                System.out.println("2라운드 끝!");
                try {
                    Thread.sleep(10000);
                    battleService.addRound2Record(gameId);
                    battleService.addGameRecord(gameId);
                    battleService.endRound2(gameId);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            };
            scheduler2.schedule(eventTask2, delayInSeconds2, TimeUnit.SECONDS);
        }
    }

    // 게임을 진행해도 되는 적합한 유저인지 체크후 게임방안에 유저 데이터를 return
    @MessageMapping("/check/{gameId}")
    public void userCheck(@DestinationVariable String gameId, @Payload String userId) {
        String failed = battleService.authJoinUser(gameId, userId);
        if (failed.equals("failed")) {
            messagingTemplate.convertAndSend("/topic/public/check/" + failed + "/" + gameId + "/" + userId, userId);
        } else {
            List<GameUserDto> gamePlayers = battleService.getGameUserDtoList(gameId);
            messagingTemplate.convertAndSend("/topic/public/refresh/user/" + gameId, gamePlayers);
        }
    }

    @MessageMapping("/get/question/{gameId}")
    public void getQuestion(@DestinationVariable String gameId, @Payload String userId) {
        // gameId 의 현재 라운드수, gameNum 을 redis에서 조회 후 RDS에서 조회후 리턴

        BattleData battleData = new BattleData();
        QuestionDto questionDto = battleService.getQuestion(gameId);
        List<TestCaseDto> testCase = battleService.getTestcase(gameId);
        battleData.setQuestionDto(questionDto);
        battleData.setTestCase(testCase);
        messagingTemplate.convertAndSend("/topic/public/get/question/" + gameId + "/" + userId, battleData);
        List<GameUserDto> gamePlayers = battleService.getGameUserDtoList(gameId);
        messagingTemplate.convertAndSend("/topic/public/refresh/user/" + gameId, gamePlayers);
    }

    @MessageMapping("/send/{gameId}")
    public void sendCode(@DestinationVariable String gameId, @Payload CodeData codeData) {
        ResultDto resultDto = battleService.runCode(codeData, gameId);
        List<TestCaseResultDto> testCaseResultDtos = resultDto.getTestCaseResultDtos();
        messagingTemplate.convertAndSend("/topic/public/get/result/" + gameId + "/" + codeData.getUserId(), testCaseResultDtos);
        List<GameUserDto> gamePlayers = battleService.getGameUserDtoList(gameId);
        messagingTemplate.convertAndSend("/topic/public/refresh/user/" + gameId, gamePlayers);

//        if (resultDto.getIsRoundEnd()) {
//            messagingTemplate.convertAndSend("/topic/public/round1/end/front/" + gameId, codeData.getUserId());
//        }
    }

    @MessageMapping("/send/1/{gameId}")
    public void sendCodeRound1(@DestinationVariable String gameId, @Payload CodeData codeData) {
        ResultDto resultDto = battleService.runCode(codeData, gameId, 1);
        List<TestCaseResultDto> testCaseResultDtos = resultDto.getTestCaseResultDtos();
        messagingTemplate.convertAndSend("/topic/public/get/result/" + gameId + "/" + codeData.getUserId(), testCaseResultDtos);

        PlayUserInfo playUserInfo = new PlayUserInfo();
        List<GameUserDto> gamePlayers = battleService.getGameUserDtoList(gameId);
        messagingTemplate.convertAndSend("/topic/public/refresh/user/" + gameId, gamePlayers);
    }
    @MessageMapping("/send/2/{gameId}")
    public void sendCodeRound2(@DestinationVariable String gameId, @Payload CodeData codeData) {
        ResultDto resultDto = battleService.runCode(codeData, gameId, 2);
        List<TestCaseResultDto> testCaseResultDtos = resultDto.getTestCaseResultDtos();
        messagingTemplate.convertAndSend("/topic/public/get/result/" + gameId + "/" + codeData.getUserId(), testCaseResultDtos);
    }

    @MessageMapping("/round1/end/{gameId}")
    public void round1End(@DestinationVariable String gameId, @Payload String userId) {
        Boolean isDisconnect = battleService.getFailedUser(gameId, userId);
        if (!isDisconnect) {
            messagingTemplate.convertAndSend("/topic/public/disconnect/user/" + gameId + "/" + userId, userId);
        } else {
            messagingTemplate.convertAndSend("/topic/public/round1/url/" + gameId + "/" + userId, userId);

            List<GameUserDto> gamePlayers = battleService.getGameUserDtoList(gameId);
            messagingTemplate.convertAndSend("/topic/public/refresh/user/" + gameId, gamePlayers);
        }
    }

    @MessageMapping("/round2/end/{gameId}")
        public void round2End(@DestinationVariable String gameId, @Payload String userId) {
            String myRank = battleService.getMyRank(gameId, userId);
            messagingTemplate.convertAndSend("/topic/public/round2/url/" + gameId + "/" + userId, myRank);
    }
}

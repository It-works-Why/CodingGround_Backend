package app.codingGround.api.battle.controller;


import app.codingGround.api.battle.dto.request.CodeData;
import app.codingGround.api.battle.dto.request.Message;
import app.codingGround.api.battle.dto.response.*;
import app.codingGround.api.battle.service.BattleService;
import app.codingGround.api.entity.Question;
import app.codingGround.global.config.model.ChatMessage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Controller
@RequiredArgsConstructor
public class BattleController {

    private final BattleService battleService;
    private final RabbitTemplate rabbitTemplate;
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
        Message message = new Message();

        if (failed.equals("failed")) {
            messagingTemplate.convertAndSend("/topic/public/getGameUsersData/" + failed + "/" + gameId + "/" + userId, playUserInfo);
        } else {
            message.setType("addUser");
            message.setGameId(gameId);
            message.setUrl("/topic/public/getGameUsersData/" + failed + "/" + gameId);
            message.setData(playUserInfo);
            rabbitTemplate.convertAndSend("game.exchange", "*.room."+gameId, message);
        }
//      게임 타입이 WAIT 이고, 유저 인원수가 8명일때! 게임시작 전송
        String gameStatus = battleService.getGameStatus(gameId);

        if (gameStatus.equals("WAIT") && userCount == 2) {
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            battleService.startGame(gameId);
            LocalDateTime currentTime = LocalDateTime.now();
            LocalDateTime futureTime = currentTime.plusSeconds(10);
            List<Question> questions = battleService.startRound(gameId);

            message.setUrl("/topic/public/gameStart/" + gameId);
            message.setType("gameStart");
            message.setGameId(gameId);
            message.setData(futureTime);
            rabbitTemplate.convertAndSend("game.exchange", "*.room."+gameId, message);

            ScheduledExecutorService scheduler1 = Executors.newScheduledThreadPool(1);
            int round1LimitMinutes = questions.get(0).getQuestionLimitTime();
            long delayInSeconds1 = round1LimitMinutes * 60L + 5L; // 현재 분을 초로 환산 후 5초를 추가합니다.

            List<GameUserDto> gameUserDtos = battleService.getGameUserDtoList(gameId);
            Runnable eventTask1 = () -> {
                System.out.println("1라운드 끝!");
                for(GameUserDto dto : gameUserDtos) {
                    Boolean isDisconnect = battleService.getFailedUser(gameId, dto.getUserId());
                    if (!isDisconnect) {
                        message.setUrl("/topic/public/disconnect/user/" + gameId + "/" + dto.getUserId());
                        message.setGameId(gameId);
                        message.setType("disconnect");
                        message.setData(dto.getUserId());
                        rabbitTemplate.convertAndSend("game.exchange", "*.room."+gameId, message);

                    } else {
                        message.setUrl("/topic/public/round1/url/" + gameId + "/" + dto.getUserId());
                        message.setType("winUser");
                        message.setGameId(gameId);
                        message.setData(dto.getUserId());
                        rabbitTemplate.convertAndSend("game.exchange", "*.room."+gameId, message);
                    }
                }
                battleService.endRound1(gameId);
                message.setData(gameId);
                message.setUrl("/topic/public/round1/end/front/" + gameId);
                message.setGameId(gameId);
                message.setType("round1End");
                rabbitTemplate.convertAndSend("game.exchange", "*.room."+gameId, message);

                List<GameUserDto> gamePlayers = battleService.getGameUserDtoList(gameId);
                message.setType("refreshUser");
                message.setData(gamePlayers);
                message.setUrl("/topic/public/refresh/user/" + gameId);
                rabbitTemplate.convertAndSend("game.exchange", "*.room."+gameId, message);


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
                message.setUrl("/topic/public/round2/end/front/" + gameId);
                message.setGameId(gameId);
                message.setType("round2End");
                message.setData(gameId);
                rabbitTemplate.convertAndSend("game.exchange", "*.room."+gameId, message);

                for(GameUserDto dto : gameUserDtos){
                    String myRank = battleService.getMyRank(gameId, dto.getUserId());
                    message.setUrl("/topic/public/round2/url/" + gameId + "/" + dto.getUserId());
                    message.setData(myRank);
                    message.setGameId(gameId);
                    message.setType("showRank");
                }

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

    @MessageMapping("/reconnect")
    public void reConnect(@Payload String userId, SimpMessageHeaderAccessor headerAccessor) {
        if (userId != null) {
            headerAccessor.getSessionAttributes().put("userId", userId);
        }
    }
}

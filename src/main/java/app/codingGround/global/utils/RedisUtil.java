package app.codingGround.global.utils;

import app.codingGround.api.battle.dto.request.ConnectGameInfo;
import app.codingGround.api.battle.dto.response.GameUserDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.util.*;

@Component
public class RedisUtil {

    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private int redisPort;

    @Value("${spring.redis.password}")
    private String redisPassword;

    public String getRedisHost() {
        return redisHost;
    }

    public int getRedisPort() {
        return redisPort;
    }

    public String getRedisPassword() {
        return redisPassword;
    }

    public String createGameRoom(ConnectGameInfo connectGameInfo, GameUserDto gameUser) {
        try {
            findConnectedGame(gameUser.getUserId());

            String gameId = findGameRoom(connectGameInfo);

            Jedis jedis = new Jedis(getRedisHost(), getRedisPort());
            jedis.auth(getRedisPassword());
            if(gameId != null){
                jedis.set(gameUser.getUserId(), gameId);
            }else{
                Map<String, String> gameRoom = new HashMap<>();
                gameId = UUID.randomUUID().toString(); // 고유 아이디
                gameRoom.put("gameId", gameId);
                gameRoom.put("gameType", connectGameInfo.getGameType());
                gameRoom.put("gameLanguage", connectGameInfo.getGameLanguage());
                gameRoom.put("gameRound", "1");
                gameRoom.put("gameMaxParticipants", "8");
//                gameRoom.put("gameParticipants", "0");
                gameRoom.put("gameStatus", "WAITING");

                // GameRoom 정보를 Redis Hash에 저장
                jedis.set(gameUser.getUserId(), gameId);
                jedis.hmset(gameId + "_gameRoom", gameRoom);
            }
            // GameRoom 안의 gameUser 필드에 참여한 유저 정보를 저장
            jedis.rpush(gameId + "_gameUsers", gameUser.getGameUser("play"));
            // Redis 연결 닫기

            jedis.close();
            return gameId;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String findGameRoom(ConnectGameInfo connectGameInfo) {
        Jedis jedis = new Jedis(getRedisHost(), getRedisPort());
        jedis.auth(getRedisPassword());

        // 찾은 게임 방 아이디를 저장할 리스트
        List<String> gameRooms = new ArrayList<>();

        // Redis에서 모든 키들을 가져오기
        Set<String> keys = jedis.keys("*");

        // 각 키에 대해 처리
        for (String key : keys) {
            if (key.endsWith("_gameRoom")) {
                // 해당 키의 gameRoom 정보 가져오기
                Map<String, String> gameRoom = jedis.hgetAll(key);

                // gameStatus가 "WAITING"이고 gameParticipants가 7 이하인 조건 체크
                String gameStatus = gameRoom.get("gameStatus");
                String gameType = gameRoom.get("gameType");
                String gameLanguage = gameRoom.get("gameLanguage");

                if ("WAITING".equals(gameStatus) && gameType.equals(connectGameInfo.getGameType()) && gameLanguage.equals(connectGameInfo.getGameLanguage())) {
                    // key에 _gameRoom 제거
                    String suffixKey = removeSuffix(key, "_gameRoom");
                    if(jedis.llen(suffixKey+"_gameUsers") < Integer.parseInt(gameRoom.get("gameMaxParticipants"))){
                        // gameStatus가 "WAITING"이고 gameParticipants가 7 이하인 경우 해당 게임 방 아이디를 리스트에 추가
                        String gameId = key.replace("_gameRoom", "");
                        gameRooms.add(gameId);
                    }
                }
            }
        }
        // Redis 연결 닫기
        jedis.close();
        if(gameRooms.isEmpty()){
            return null;
        }
        return gameRooms.get(0);
    }


    public String findConnectedGame(String userId){
        Jedis jedis = new Jedis(getRedisHost(), getRedisPort());
        jedis.auth(getRedisPassword());

        String gameRoomKey = jedis.get(userId);

        List<String> results = jedis.lrange(gameRoomKey+"_gameUsers", 0, -1);

        return null;
    }
    public String removeSuffix(String input, String suffix) {
        if (input != null && input.endsWith(suffix)) {
            return input.substring(0, input.length() - suffix.length());
        }
        return input;
    }
}

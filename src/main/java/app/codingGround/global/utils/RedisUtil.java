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

//    public String createGameRoom(ConnectGameInfo connectGameInfo, GameUserDto gameUser) {
//        try {
//            findConnectedGame(gameUser.getUserId());
//
//            String gameId = findGameRoom(connectGameInfo);
//
//            Jedis jedis = new Jedis(getRedisHost(), getRedisPort());
//            jedis.auth(getRedisPassword());
//            if(gameId != null){
//
//            }else{
//
//
//
//            }
//            // GameRoom 안의 gameUser 필드에 참여한 유저 정보를 저장
//            // Redis 연결 닫기
//
//            jedis.close();
//            return gameId;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }




    // 원하는 단어 때기
    public String removeSuffix(String input, String suffix) {
        if (input != null && input.endsWith(suffix)) {
            return input.substring(0, input.length() - suffix.length());
        }
        return input;
    }

    // 재접속 여부 판단
    public String findConnectedGame(String userId){
        Jedis jedis = new Jedis(getRedisHost(), getRedisPort());
        jedis.auth(getRedisPassword());

        Map<String, String> userKey = jedis.hgetAll(userId);
        if(userKey.get("gameId") != null){
            String userKeyGameStatus = userKey.get("status");
            if(userKeyGameStatus.equals("DISCONNECT")){
                return userKey.get("gameId");
            }
        }
        return null;
    }



    // 참가 가능한 게임방 검색 후 방 키 RETURN 없으면 NULL
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
        jedis.close();
        if(gameRooms.isEmpty()){
            return null;
        }else{
            return gameRooms.get(0);
        }
    }


    // 방생성
    public String createRoom(ConnectGameInfo connectGameInfo) {
        Jedis jedis = new Jedis(getRedisHost(), getRedisPort());
        jedis.auth(getRedisPassword());
        Map<String, String> gameRoom = new HashMap<>();
        String gameId = UUID.randomUUID().toString(); // 고유 아이디
        gameRoom.put("gameId", gameId);
        gameRoom.put("gameType", connectGameInfo.getGameType());
        gameRoom.put("gameLanguage", connectGameInfo.getGameLanguage());
        gameRoom.put("gameRound", "1");
        gameRoom.put("gameMaxParticipants", "8");
//        gameRoom.put("gameParticipants", "0");
        gameRoom.put("gameStatus", "WAIT"); // WAIT PLAY
        jedis.hmset(gameId + "_gameRoom", gameRoom);
        jedis.close();
        return gameId;
    }





    // 게임서비스를 이용중인 유저 정보 저장
    public void createUserKey(String gameId, GameUserDto gameUserDto) {
        Jedis jedis = null;
        try {
            jedis = new Jedis(getRedisHost(), getRedisPort());
            jedis.auth(getRedisPassword());
            Map<String, String> userKey = new HashMap<>();
            userKey.put("gameId", gameId);
            userKey.put("status", "DISCONNECT"); // WAITING PLAYING DISCONNECT
            // GameRoom 정보를 Redis Hash에 저장
            jedis.hmset(gameUserDto.getUserId(), userKey);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }





    // 유저 참여
    public void joinGameRoom(String gameId, GameUserDto gameUserDto){
        Jedis jedis = new Jedis(getRedisHost(), getRedisPort());
        Map<String, String> userKey = new HashMap<>();

        jedis.auth(getRedisPassword());
        jedis.rpush(gameId + "_gameUsers", gameUserDto.getGameUser("")); // 1, 2, 3, 4, 5 OR 'DEFEAT'
        userKey.put("gameId", gameId);
        userKey.put("status", "WAIT"); // WAIT PLAY
    }



    // 재접속 수락
    public void acceptReconnect(String userId) {
        Jedis jedis = new Jedis(getRedisHost(), getRedisPort());
        jedis.auth(getRedisPassword());
        Map<String, String> userKey = jedis.hgetAll(userId);
        String gameKey = userKey.get("gameKey");
        userKey.put("status", "PLAYING");
        jedis.hmset(userId, userKey);
    }




    // 재접속 거절 (탈락처리)
    public void denyReconnect(String userId){
        Jedis jedis = new Jedis(getRedisHost(), getRedisPort());
        jedis.auth(getRedisPassword());
        jedis.del(userId);
    }




    // 탈주 (탈주처리)


}

package app.codingGround.global.utils;

import app.codingGround.api.battle.dto.request.ConnectGameInfo;
import app.codingGround.api.battle.dto.response.GameUserDto;
import app.codingGround.api.battle.dto.response.PersonalGameDataDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.parameters.P;
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
    // 유저 해시 테이블이 존재하는지 확인하고, 'DISCONNECT'인지 판단후 gameId 또는 null 리턴
    public String findConnectedGame(String userId){
        Jedis jedis = new Jedis(getRedisHost(), getRedisPort());
        jedis.auth(getRedisPassword());

        Map<String, String> personalGameData = jedis.hgetAll(userId);
        if(personalGameData.get("gameId") != null){
            String userKeyGameStatus = personalGameData.get("status");
            if(userKeyGameStatus.equals("DISCONNECT")){
                if (jedis != null) {
                    jedis.close();
                }
                return personalGameData.get("gameId");
            }
        }
        if (jedis != null) {
            jedis.close();
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

                if ("WAIT".equals(gameStatus) && gameType.equals(connectGameInfo.getGameType()) && gameLanguage.equals(connectGameInfo.getGameLanguage())) {
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
            userKey.put("status", "WAITING"); // WAITING PLAYING DISCONNECT
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
        jedis.auth(getRedisPassword());

        jedis.rpush(gameId + "_gameUsers", gameUserDto.getGameUser("DEFAULT")); // 1, 2, 3, 4, 5 OR 'DEFEAT', 'DEFAULT'
        if (jedis != null) {
            jedis.close();
        }
    }



    // 재접속 수락
    public void acceptReconnect(String userId) {
        Jedis jedis = new Jedis(getRedisHost(), getRedisPort());
        jedis.auth(getRedisPassword());
        Map<String, String> userKey = jedis.hgetAll(userId);
        userKey.put("status", "PLAYING");
        jedis.hmset(userId, userKey);
        if (jedis != null) {
            jedis.close();
        }
    }




    // 재접속 거절 (탈락처리)
    public void denyReconnect(String userId){
        Jedis jedis = new Jedis(getRedisHost(), getRedisPort());
        jedis.auth(getRedisPassword());
        jedis.del(userId);
        if (jedis != null) {
            jedis.close();
        }
    }

    // 게임방 사용자 수 체크 로직
    public long getUserCount(String gameId) {
        Jedis jedis = new Jedis(getRedisHost(), getRedisPort());
        jedis.auth(getRedisPassword());
        long result = jedis.llen(gameId+"_gameUsers");
        if (jedis != null) {
            jedis.close();
        }
        return result;
    }

    public List<GameUserDto> getGameUserDtoList(String gameId) {
        Jedis jedis = new Jedis(getRedisHost(), getRedisPort());
        jedis.auth(getRedisPassword());
        String listKey = gameId+"_gameUsers";
        List<GameUserDto> gameUserDtoList = GameUserDto.parseToGameUserList(jedis.lrange(listKey,0,-1).toString());
        if (jedis != null) {
            jedis.close();
        }
        return gameUserDtoList;
    }

    // 현재 게임중인 usersList에서 내가 존재하는지 AND 목록에 userGameResult가 DEFAULT 인지 확인 로직
    // 악성유자가 gameId 를 탈취 했을때, 그방에 못들어가게하는 로직.
    public Boolean authJoinUser(String gameId, String userId) {
        Jedis jedis = new Jedis(getRedisHost(), getRedisPort());
        jedis.auth(getRedisPassword());
        String listKey = gameId+"_gameUsers";
        List<GameUserDto> gameUserDtoList = GameUserDto.parseToGameUserList(jedis.lrange(listKey,0,-1).toString());
        jedis.close();;
        for(GameUserDto dto : gameUserDtoList){
            if(dto.getUserId().equals(userId) && dto.getUserGameResult().equals("DEFAULT")){
                return true;
            }
        }
        return false;
    }

    public PersonalGameDataDto findGameKeyByUserId(String userId) {
        Jedis jedis = new Jedis(getRedisHost(), getRedisPort());
        jedis.auth(getRedisPassword());

        Map<String, String> personalGameData = jedis.hgetAll(userId);
        jedis.close();
        return PersonalGameDataDto.builder()
                .userId(userId)
                .gameId(personalGameData.get("gameId"))
                .status(personalGameData.get("status"))
                .build();
    }

    public void disconnectGameFromIngame(String userId) {
        Jedis jedis = new Jedis(getRedisHost(), getRedisPort());
        jedis.auth(getRedisPassword());
        Map<String, String> userKey = jedis.hgetAll(userId);
        userKey.put("status", "DISCONNECT");
        jedis.hmset(userId, userKey);
        jedis.close();
    }

    public void allRemoveRelatedUserId(PersonalGameDataDto personalGameDataDto) {
        Jedis jedis = new Jedis(getRedisHost(), getRedisPort());
        jedis.auth(getRedisPassword());

        List<GameUserDto> gameUserDtoList = getGameUserDtoList(personalGameDataDto.getGameId());
        String gameUserDtoToString = null;
        for(GameUserDto dto : gameUserDtoList){
            if(dto.getUserId().equals(personalGameDataDto.getUserId())){
                gameUserDtoToString = dto.getGameUser();
                break;
            }
        }
        jedis.lrem(personalGameDataDto.getGameId()+"_gameUsers",0,gameUserDtoToString);
        jedis.del(personalGameDataDto.getUserId());
        Long usersCount = jedis.llen(personalGameDataDto.getGameId()+"_gameUsers");
        if(usersCount == 0 || usersCount == null){
            jedis.del(personalGameDataDto.getGameId()+"_gameRoom");
        }
        jedis.close();
    }


    // 탈주 (탈주처리)


}

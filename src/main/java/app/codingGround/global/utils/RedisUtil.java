package app.codingGround.global.utils;

import app.codingGround.api.battle.dto.request.ConnectGameInfo;
import app.codingGround.api.battle.dto.response.GameDto;
import app.codingGround.api.battle.dto.response.GameUserDto;
import app.codingGround.api.battle.dto.response.PersonalGameDataDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.sql.Timestamp;
import java.time.LocalDateTime;
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


    private Jedis getJedisInstance() {
        Jedis jedis = new Jedis(getRedisHost(), getRedisPort());
        jedis.auth(getRedisPassword());
        return jedis;
    }

    private void closeJedisInstance(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }

    // 원하는 단어 때기
    public String removeSuffix(String input, String suffix) {
        if (input != null && input.endsWith(suffix)) {
            return input.substring(0, input.length() - suffix.length());
        }
        return input;
    }

    // 재접속 여부 판단
    // 유저 해시 테이블이 존재하는지 확인하고, 'DISCONNECT'인지 판단후 gameId 또는 null 리턴
    public String findConnectedGame(String userId) {
        Jedis jedis = null;
        try {
            jedis = getJedisInstance();

            Map<String, String> personalGameData = jedis.hgetAll(userId);
            if (personalGameData.get("gameId") != null) {
                String userKeyGameStatus = personalGameData.get("status");
                if (userKeyGameStatus.equals("DISCONNECT")) {
                    return personalGameData.get("gameId");
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            closeJedisInstance(jedis);
        }
    }


    // 참가 가능한 게임방 검색 후 방 키 RETURN 없으면 NULL
    public String findGameRoom(ConnectGameInfo connectGameInfo) {
        Jedis jedis = null;
        try {
            jedis = getJedisInstance();

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
                        if (jedis.llen(suffixKey + "_gameUsers") < Integer.parseInt(gameRoom.get("gameMaxParticipants"))) {
                            // gameStatus가 "WAITING"이고 gameParticipants가 7 이하인 경우 해당 게임 방 아이디를 리스트에 추가
                            String gameId = key.replace("_gameRoom", "");
                            gameRooms.add(gameId);
                        }
                    }
                }
            }
            if (gameRooms.isEmpty()) {
                return null;
            } else {
                return gameRooms.get(0);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            closeJedisInstance(jedis);
        }
    }


    // 방생성
    public String createRoom(ConnectGameInfo connectGameInfo) {
        Jedis jedis = null;
        try {
            jedis = getJedisInstance();
            Map<String, String> gameRoom = new HashMap<>();
            String gameId = UUID.randomUUID().toString(); // 고유 아이디
            gameRoom.put("gameId", gameId);
            gameRoom.put("gameType", connectGameInfo.getGameType());
            gameRoom.put("gameLanguage", connectGameInfo.getGameLanguage());
            gameRoom.put("gameRound", "1");
            gameRoom.put("gameMaxParticipants", "8");
            gameRoom.put("firstRoundEndTime", String.valueOf(Timestamp.valueOf(LocalDateTime.now())));
            gameRoom.put("secondRoundEndTime", String.valueOf(Timestamp.valueOf(LocalDateTime.now())));
//        gameRoom.put("gameParticipants", "0");
            gameRoom.put("gameStatus", "WAIT"); // WAIT PLAY
            gameRoom.put("gameNum", "0");
            jedis.hmset(gameId + "_gameRoom", gameRoom);
            return gameId;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            closeJedisInstance(jedis);
        }
    }

    // 게임서비스를 이용중인 유저 정보 저장
    public void createUserKey(String gameId, GameUserDto gameUserDto) {
        Jedis jedis = null;
        try {
            jedis = getJedisInstance();
            Map<String, String> userKey = new HashMap<>();
            userKey.put("gameId", gameId);
            userKey.put("status", "WAITING"); // WAITING PLAYING DISCONNECT
            // GameRoom 정보를 Redis Hash에 저장
            jedis.hmset(gameUserDto.getUserId(), userKey);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            closeJedisInstance(jedis);
        }
    }


    // 유저 참여
    public void joinGameRoom(String gameId, GameUserDto gameUserDto) {
        Jedis jedis = null;
        try {
            jedis = getJedisInstance();
            jedis.rpush(gameId + "_gameUsers", gameUserDto.getGameUser()); // 1, 2, 3, 4, 5 OR 'DEFEAT', 'DEFAULT'
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            closeJedisInstance(jedis);
        }
    }


    // 재접속 수락
    public void acceptReconnect(String userId) {
        Jedis jedis = null;
        try {
            jedis = getJedisInstance();
            Map<String, String> userKey = jedis.hgetAll(userId);
            userKey.put("status", "PLAYING");
            jedis.hmset(userId, userKey);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            closeJedisInstance(jedis);
        }

    }


    // 재접속 거절 (탈락처리)
    public void denyReconnect(String userId) {
        Jedis jedis = null;
        try {
            jedis = getJedisInstance();
            jedis.del(userId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            closeJedisInstance(jedis);
        }
    }

    // 게임방 사용자 수 체크 로직
    public long getUserCount(String gameId) {
        Jedis jedis = null;
        try {
            jedis = getJedisInstance();
            long result = jedis.llen(gameId + "_gameUsers");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            closeJedisInstance(jedis);
        }
    }

    public List<GameUserDto> getGameUserDtoResult(String gameId) {
        Jedis jedis = null;
        try {
            jedis = getJedisInstance();
            String listKey = gameId + "_gameUsers";
            List<GameUserDto> gameUserDtoList = GameUserDto.parseToGameUserList(jedis.lrange(listKey, 0, -1).toString());
            return gameUserDtoList;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            closeJedisInstance(jedis);
        }
    }

    public List<GameUserDto> getGameUserDtoList(String gameId) {
        Jedis jedis = null;
        try {
            jedis = getJedisInstance();
            String listKey = gameId + "_gameUsers";
            List<GameUserDto> gameUserDtoList = GameUserDto.parseToGameUserList(jedis.lrange(listKey, 0, -1).toString());
            int i = 0;
            for(GameUserDto dto : gameUserDtoList){
                Map<String, String> personalGameData = jedis.hgetAll(dto.getUserId());
                if (personalGameData.get("gameId") != null) {
                    String userKeyGameStatus = personalGameData.get("status");
                    if (userKeyGameStatus.equals("DISCONNECT")) {
                        gameUserDtoList.get(i).setUserGameResult("DISCONNECT");
                    }
                }
                i++;
            }
            return gameUserDtoList;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            closeJedisInstance(jedis);
        }
    }


    // 현재 게임중인 usersList에서 내가 존재하는지
    // 악성유자가 gameId 를 탈취 했을때, 그방에 못들어가게하는 로직.
    public Boolean authJoinUser(String gameId, String userId) {
        Jedis jedis = null;
        try {
            jedis = getJedisInstance();
            String listKey = gameId + "_gameUsers";
            List<GameUserDto> gameUserDtoList = GameUserDto.parseToGameUserList(jedis.lrange(listKey, 0, -1).toString());

            for (GameUserDto dto : gameUserDtoList) {
                if (dto.getUserId().equals(userId)) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            closeJedisInstance(jedis);
        }
    }

    public PersonalGameDataDto findGameKeyByUserId(String userId) {
        Jedis jedis = null;
        try {
            jedis = getJedisInstance();

            Map<String, String> personalGameData = jedis.hgetAll(userId);
            return PersonalGameDataDto.builder().userId(userId).gameId(personalGameData.get("gameId")).status(personalGameData.get("status")).build();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            closeJedisInstance(jedis);
        }
    }

    public void disconnectGameFromIngame(String userId) {
        Jedis jedis = null;
        try {
            jedis = getJedisInstance();
            Map<String, String> userKey = jedis.hgetAll(userId);
            userKey.put("status", "DISCONNECT");
            jedis.hmset(userId, userKey);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            closeJedisInstance(jedis);
        }
    }

    public void allRemoveRelatedUserId(PersonalGameDataDto personalGameDataDto) {
        Jedis jedis = null;
        try {
            jedis = getJedisInstance();

            List<GameUserDto> gameUserDtoList = getGameUserDtoResult(personalGameDataDto.getGameId());
            String gameUserDtoToString = null;
            for (GameUserDto dto : gameUserDtoList) {
                if (dto.getUserId().equals(personalGameDataDto.getUserId())) {
                    gameUserDtoToString = dto.getGameUser();
                    break;
                }
            }
            jedis.lrem(personalGameDataDto.getGameId() + "_gameUsers", 0, gameUserDtoToString);
            jedis.del(personalGameDataDto.getUserId());
            Long usersCount = jedis.llen(personalGameDataDto.getGameId() + "_gameUsers");
            if (usersCount == 0 || usersCount == null) {
                jedis.del(personalGameDataDto.getGameId() + "_gameRoom");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            closeJedisInstance(jedis);
        }
    }

    public String findTryConnectSameUserId(String userId) {
        Jedis jedis = null;

        try {
            jedis = getJedisInstance();
            Map<String, String> userKey = jedis.hgetAll(userId);
            String gameId = null;
            if (userKey != null && userKey.containsKey("status") && userKey.get("status").equals("WAITING")) {
                gameId = userKey.get("gameId");
            }
            return gameId;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            closeJedisInstance(jedis);
        }
    }

    public String getGameId(String userId) {
        Jedis jedis = null;
        try {
            jedis = getJedisInstance();
            Map<String, String> joiningGame = jedis.hgetAll(userId);
            return joiningGame.get("gameId");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            closeJedisInstance(jedis);
        }
    }

    public GameDto findGameByGameId(String gameId) {
        Jedis jedis = null;

        GameDto gameDto = null;
        try {
            jedis = getJedisInstance();
            Map<String, String> game = jedis.hgetAll(gameId + "_gameRoom");
            gameDto = new GameDto();
            gameDto.setGameLanguage(Integer.valueOf(game.get("gameLanguage")));
            gameDto.setGameStatus(game.get("gameStatus"));
            gameDto.setGameRound(Integer.parseInt(game.get("gameRound")));
            gameDto.setGameId(gameId);
            gameDto.setGameType(game.get("gameType"));
            gameDto.setGameNum(Long.valueOf(game.get("gameNum")));
            gameDto.setGameMaxParticipants(Long.parseLong(game.get("gameMaxParticipants")));
            gameDto.setFirstRoundEndTime(Timestamp.valueOf(game.get("firstRoundEndTime")));
            gameDto.setSecondRoundEndTime(Timestamp.valueOf(game.get("secondRoundEndTime")));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            closeJedisInstance(jedis);
        }
        return gameDto;
    }

    public void updateGameData(GameDto gameDto) {
        Jedis jedis = null;
        try {
            if(gameDto.getFirstRoundEndTime() == null){
                gameDto.setFirstRoundEndTime(Timestamp.valueOf(LocalDateTime.now()));
            }
            if(gameDto.getSecondRoundEndTime() == null){
                gameDto.setSecondRoundEndTime(Timestamp.valueOf(LocalDateTime.now()));
            }
            jedis = getJedisInstance();
            Map<String, String> gameRoom = new HashMap<>();
            gameRoom.put("gameId", gameDto.getGameId());
            gameRoom.put("gameType", gameDto.getGameType());
            gameRoom.put("gameLanguage", String.valueOf(gameDto.getGameLanguage()));
            gameRoom.put("gameRound", String.valueOf(gameDto.getGameRound()));
            gameRoom.put("gameMaxParticipants", String.valueOf(gameDto.getGameMaxParticipants()));
            gameRoom.put("gameStatus", gameDto.getGameStatus()); // WAIT PLAY
            gameRoom.put("gameNum", String.valueOf(gameDto.getGameNum()));
            gameRoom.put("firstRoundEndTime", String.valueOf(gameDto.getFirstRoundEndTime()));
            gameRoom.put("secondRoundEndTime", String.valueOf(gameDto.getSecondRoundEndTime()));
            jedis.hmset(gameDto.getGameId() + "_gameRoom", gameRoom);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            closeJedisInstance(jedis);
        }
    }

    public void updateUserGameStatus(List<GameUserDto> list, String gameStatus, String gameId) {
        Jedis jedis = null;
        try {
            Map<String, String> userGameInfo = null;
            jedis = getJedisInstance();
            for(GameUserDto dto : list){
                userGameInfo = new HashMap<>();
                userGameInfo.put("gameId", gameId);
                userGameInfo.put("status", gameStatus);
                jedis.hmset(dto.getUserId(),userGameInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            closeJedisInstance(jedis);
        }
    }

    public void updateUserStatus(String gameId, String userId, String number) {
        Jedis jedis = null;

        try {
            jedis = getJedisInstance();
            long length = jedis.llen(gameId+"_gameUsers");
            for (long i = 0; i < length; i++) {
                GameUserDto userDto = GameUserDto.parseToGameUser(jedis.lindex(gameId+"_gameUsers", i));
                if(userDto.getUserId().equals(userId) && userDto.getUserGameResult().equals("DEFAULT") || userDto.getUserId().equals(userId) && userDto.getUserGameResult().equals("5")){
                    userDto.setUserGameResult(number);

                    String updateData = userDto.getGameUser();
                    jedis.lset(gameId + "_gameUsers", i, updateData);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            closeJedisInstance(jedis);
        }
    }

    public void updateUserStatus(String gameId, String userId, String number, String memory) {
        Jedis jedis = null;
        try {
            jedis = getJedisInstance();
            long length = jedis.llen(gameId+"_gameUsers");
            for (long i = 0; i < length; i++) {
                GameUserDto userDto = GameUserDto.parseToGameUser(jedis.lindex(gameId+"_gameUsers", i));
                if(userDto.getUserId().equals(userId)){
                    userDto.setUserGameResult(number);
                    userDto.setMemory(memory);
                    String updateData = userDto.getGameUser();
                    jedis.lset(gameId + "_gameUsers", i, updateData);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            closeJedisInstance(jedis);
        }
    }

    public void updateRound(String gameId) {
        GameDto gameDto = findGameByGameId(gameId);
        gameDto.setGameRound(2);
        updateGameData(gameDto);
    }

    public void removeGame(String gameId) {
        Jedis jedis = null;
        try {
            jedis = getJedisInstance();
            List<GameUserDto> gameUserDtoList = getGameUserDtoResult(gameId);
            for (GameUserDto dto : gameUserDtoList) {
                jedis.del(dto.getUserId());
            }
            jedis.del(gameId + "_gameUsers");
            jedis.del(gameId + "_gameRoom");
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            closeJedisInstance(jedis);
        }
    }


    // 탈주 (탈주처리)


}

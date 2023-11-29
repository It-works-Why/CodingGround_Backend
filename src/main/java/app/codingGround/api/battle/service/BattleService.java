package app.codingGround.api.battle.service;

import app.codingGround.api.battle.dto.request.ConnectGameInfo;
import app.codingGround.api.battle.repository.BattleRoomRepository;
import app.codingGround.api.entity.Language;
import app.codingGround.global.utils.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BattleService {
    //jpa
    private final BattleRoomRepository languageRepository;

    private final RedisTemplate<String, String> redisTemplate;

//    userId { gameRoomId }, userId { gameRoomId }


    public String createGameRoom(ConnectGameInfo connectGameInfo, String accessToken) {
        try {
            String gameId = findGameRoom(connectGameInfo);

            if(gameId != null){
                return gameId;
            }
            String userId = JwtTokenProvider.getUserId(accessToken);
            Jedis jedis = new Jedis("airspirk.asuscomm.com", 46379);
            jedis.auth("root");
            // 게임 방 정보를 저장할 Map 생성
            Map<String, String> gameRoom = new HashMap<>();
            gameId = UUID.randomUUID().toString(); // 고유 아이디
            gameRoom.put("gameId", gameId);
            gameRoom.put("gameType", connectGameInfo.getGameType());
            gameRoom.put("gameLanguage", connectGameInfo.getGameLanguage());
            gameRoom.put("gameRound", "1");
            gameRoom.put("gameMaxParticipants", "8");
            gameRoom.put("gameParticipants", "0");
            gameRoom.put("gameStatus", "WAITING");

            // 게임 방에 참여한 유저 정보를 저장할 List 생성
//            List<Map<String, String>> gameUsers = new ArrayList<>();

            // 유저 1 정보
//        Map<String, String> user1 = new HashMap<>();
//        user1.put("userId", "1");
//        user1.put("userNickname", "Player1");
//        user1.put("userProfileImgUrl", "url_to_profile_image_1");
//        user1.put("userGameStatus", "");  // 1, 2, 3, 4 ,
//        gameUsers.add(user1);

            // GameRoom 정보를 Redis Hash에 저장
            jedis.set(userId, gameId);
            jedis.hmset(gameId + "_gameRoom", gameRoom);

            // GameRoom 안의 gameUser 필드에 참여한 유저 정보를 저장
//            for (int i = 0; i < gameUsers.size(); i++) {
//                jedis.rpush("GameRoom:" + gameId + ":gameUser", String.valueOf(gameUsers.get(i)));
//            }
            // Redis 연결 닫기
            jedis.close();
            return gameId;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String findGameRoom(ConnectGameInfo connectGameInfo) {
        Jedis jedis = new Jedis("airspirk.asuscomm.com", 46379);
        jedis.auth("root");

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
                int gameParticipants = Integer.parseInt(gameRoom.get("gameMaxParticipants"));

                if ("WAITING".equals(gameStatus) && gameParticipants <= 7 && gameType.equals(connectGameInfo.getGameType()) && gameLanguage.equals(connectGameInfo.getGameLanguage())) {
                    // gameStatus가 "WAITING"이고 gameParticipants가 7 이하인 경우 해당 게임 방 아이디를 리스트에 추가
                    String gameId = key.replace("_gameRoom", "");
                    gameRooms.add(gameId);
                }
            }
        }
        // Redis 연결 닫기
        jedis.close();
        if(gameRooms.size() == 0){
            return null;
        }
        return gameRooms.get(0);
    }

    public List<Language> getLanguage() {
        try {
            return languageRepository.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


}




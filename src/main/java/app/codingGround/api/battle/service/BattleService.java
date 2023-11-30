package app.codingGround.api.battle.service;

import app.codingGround.api.battle.dto.request.ConnectGameInfo;
import app.codingGround.api.battle.dto.response.GameUserDto;
import app.codingGround.api.battle.dto.response.QueueInfoDto;
import app.codingGround.api.battle.repository.LanguageRepository;
import app.codingGround.api.entity.Language;
import app.codingGround.api.entity.User;
import app.codingGround.api.user.repository.UserRepository;
import app.codingGround.domain.common.dto.response.DefaultResultDto;
import app.codingGround.global.utils.JwtTokenProvider;
import app.codingGround.global.utils.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BattleService {
    //jpa
    private final LanguageRepository languageRepository;
    private final UserRepository userRepository;
    private final RedisUtil redisUtil;

//    userId { gameRoomId }, userId { gameRoomId }

    public List<Language> getLanguage() {
        try {
            return languageRepository.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    public QueueInfoDto tryGameConnect(ConnectGameInfo connectGameInfo, String accessToken) {
        String userId = JwtTokenProvider.getUserId(accessToken);
        User user = userRepository.findByUserId(userId);
        GameUserDto gameUserDto = GameUserDto.builder()
                                  .userNickname(user.getUserNickname())
                                  .profileImg(user.getUserProfileImg())
                                  .userId(userId)
                                  .build();
        System.out.println("start");
        String isReconnect = redisUtil.findConnectedGame(user.getUserId());
        if(isReconnect != null){
            System.out.println("재접속가능 리턴");
            return QueueInfoDto.builder().gameId(isReconnect).isReconnect(true).build();
        }
        // 여기서부터는 재접속이 필요없는 유저
        String gameId = redisUtil.findGameRoom(connectGameInfo);
        System.out.println(gameId + "방 키임");
        if(gameId == null){
            System.out.println("널이라서 새로만듬 방");
            gameId = redisUtil.createRoom(connectGameInfo);
        }
        redisUtil.createUserKey(gameId, gameUserDto);
        redisUtil.joinGameRoom(gameId, gameUserDto);
        System.out.println("저장댐");
        return QueueInfoDto.builder().gameId(gameId).isReconnect(false).build();
    }

    public DefaultResultDto acceptReconnect(String accessToken) {
        String userId = JwtTokenProvider.getUserId(accessToken);
        redisUtil.acceptReconnect(userId);

        return DefaultResultDto.builder().success(true).message("재접속 성공").build();
    }

    public DefaultResultDto denyReconnect(String accessToken) {
        String userId = JwtTokenProvider.getUserId(accessToken);
        redisUtil.denyReconnect(userId);

        return DefaultResultDto.builder().success(true).message("재접속 거절").build();
    }


}




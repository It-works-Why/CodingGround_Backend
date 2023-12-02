package app.codingGround.api.battle.service;

import app.codingGround.api.battle.dto.request.ConnectGameInfo;
import app.codingGround.api.battle.dto.response.GameUserDto;
import app.codingGround.api.battle.dto.response.PersonalGameDataDto;
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
        String isReconnect = redisUtil.findConnectedGame(user.getUserId());
        if(isReconnect != null){
            return QueueInfoDto.builder().gameId(isReconnect).connectType("reConnect").build();
        }

        // 큐를 잡고있는사람이 게임방 입장 할려고 할때 연결 거부
        String isWaiting = redisUtil.findTryConnectSameUserId(userId);
        if(isWaiting != null){
            return QueueInfoDto.builder().gameId(isWaiting).connectType("failed").build();
        }

        // 여기서부터는 재접속이 필요없는 유저
        String gameId = redisUtil.findGameRoom(connectGameInfo);
        if(gameId == null){
            gameId = redisUtil.createRoom(connectGameInfo);
        }
        redisUtil.createUserKey(gameId, gameUserDto);
        redisUtil.joinGameRoom(gameId, gameUserDto);
        return QueueInfoDto.builder().gameId(gameId).connectType("succeed").build();
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

    public long getUserCount(String gameId) {
        return redisUtil.getUserCount(gameId);
    }


    public List<GameUserDto> getGameUserDtoList(String gameId) {
        return redisUtil.getGameUserDtoList(gameId);
    }

    public String authJoinUser(String gameId, String userId) {
        String isSuccess = "";
        if(redisUtil.authJoinUser(gameId, userId)) {
            isSuccess = "succeed";
        }else{
            isSuccess = "failed";
        }
        return isSuccess;
    }

    public void escapeGame(String userId) {
        PersonalGameDataDto personalGameDataDto = redisUtil.findGameKeyByUserId(userId);
        if(personalGameDataDto.getStatus().equals("PLAYING")){
            redisUtil.disconnectGameFromIngame(userId);
        }
        if(personalGameDataDto.getStatus().equals("WAITING")){
            redisUtil.allRemoveRelatedUserId(personalGameDataDto);
        }
    }
}




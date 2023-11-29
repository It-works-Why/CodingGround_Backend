package app.codingGround.api.battle.service;

import app.codingGround.api.battle.dto.request.ConnectGameInfo;
import app.codingGround.api.battle.dto.response.GameUserDto;
import app.codingGround.api.battle.repository.LanguageRepository;
import app.codingGround.api.entity.Language;
import app.codingGround.api.entity.User;
import app.codingGround.api.user.repository.UserRepository;
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


    public String createGameRoom(ConnectGameInfo connectGameInfo, String accessToken) {
        String userId = JwtTokenProvider.getUserId(accessToken);
        User user = userRepository.findByUserId(userId);


        return redisUtil.createGameRoom(connectGameInfo, GameUserDto.builder()
                                                        .userNickname(user.getUserNickname())
                                                        .profileImg(user.getUserProfileImg())
                                                        .userId(userId)
                                                        .build());
    }
}




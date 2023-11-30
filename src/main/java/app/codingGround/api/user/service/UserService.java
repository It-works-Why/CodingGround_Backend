package app.codingGround.api.user.service;

import app.codingGround.api.user.dto.response.*;
import app.codingGround.api.user.mapper.UserMapper;
import app.codingGround.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;
    public InfoDto getUserInfo(String userId) {
        return userMapper.getUserInfo(userId);
    }
    public List<RankingDto> getUserRankings(String userId) {
        return userMapper.getUserRankings(userId);
    }
    public List<GameBadgeDto> getUserBadge(String userId) {
        return userMapper.getUserBadge(userId);
    }
    public List<GameLanguageDto> getUserGameLanguage(String userId) {
        return userMapper.UserGameLanguage(userId);
    }
    public List<GameInfoDto> getUserGameInfo(String userId){
        List<GameInfoDto> gameInfoList = userMapper.getUserGameInfo(userId);

        for (GameInfoDto gameInfoDto : gameInfoList) {
            String userNicknamesString = gameInfoDto.getUserNicknames();
            String userProfileImgString = gameInfoDto.getUserProfileImgs();
            List<String> userProfileImgList = Arrays.asList(userProfileImgString.split(","));
            List<String> userNicknamesList = Arrays.asList(userNicknamesString.split(","));
            gameInfoDto.setUserNicknamesList(userNicknamesList);
            gameInfoDto.setUserProfileImgList(userProfileImgList);
        }
        return  gameInfoList;
    }
}
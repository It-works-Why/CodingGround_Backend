package app.codingGround.api.user.service;

import app.codingGround.api.entity.User;
import app.codingGround.api.user.dto.response.Ranking;
import app.codingGround.api.user.dto.response.UserInfoVo;
import app.codingGround.api.user.mapper.UserMapper;
import app.codingGround.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;
    public UserInfoVo getUserInfo(String userId) {
        User user = userRepository.findById(userId);
        List<Ranking> rankingList = userMapper.getMyRanking(userId);







        return UserInfoVo.builder()
                .userProfileImg(user.getUserProfileImg())
                .userAffiliationDetail(user.getUserAffiliationDetail())
                .userNickname(user.getUserNickname())
                .build();
    }
}
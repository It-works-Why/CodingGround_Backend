package app.codingGround.api.user.service;

import app.codingGround.api.user.dto.response.GameBadgeDto;
import app.codingGround.api.user.dto.response.InfoDto;
import app.codingGround.api.user.dto.response.RankingDto;
import app.codingGround.api.user.mapper.UserMapper;
import app.codingGround.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
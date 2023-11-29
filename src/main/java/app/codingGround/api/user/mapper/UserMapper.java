package app.codingGround.api.user.mapper;

import app.codingGround.api.user.dto.response.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {

    InfoDto getUserInfo(String userId);

    List<RankingDto> getUserRankings(String userId);

    List<GameBadgeDto> getUserBadge(String userId);
    List<GameLanguageDto> UserGameLanguage(String userId);

}

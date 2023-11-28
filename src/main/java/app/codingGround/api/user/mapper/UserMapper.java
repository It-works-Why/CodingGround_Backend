package app.codingGround.api.user.mapper;

import app.codingGround.api.user.dto.response.RankingDto;
import app.codingGround.api.user.dto.response.UserInfoDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {

    UserInfoDto getUserInfo(String userId);

    /*List<RankingDto> getUserRankings(String userId);*/

}

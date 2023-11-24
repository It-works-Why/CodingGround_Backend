package app.codingGround.api.user.mapper;

import app.codingGround.api.user.dto.response.Ranking;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {

    List<Ranking> getMyRanking(String userId);

}

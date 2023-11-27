package app.codingGround.api.user.mapper;

import app.codingGround.api.user.dto.response.RankListDto;
import app.codingGround.api.user.dto.response.Ranking;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RankingMapper {

    List<RankListDto> getRankingList();

}

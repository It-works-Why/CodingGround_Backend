package app.codingGround.api.ranking.mapper;

import app.codingGround.api.ranking.dto.response.RankListDto;
import app.codingGround.api.ranking.dto.response.SeasonListDto;
import app.codingGround.api.user.dto.response.PageNumDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RankingMapper {

    List<RankListDto> getRankingList(int rankingPageNum);
    List<SeasonListDto> getSeasonList();
    PageNumDto getTotalPage();
    PageNumDto getSeasonTotalPage();
    PageNumDto getKeywordTotalPage();

    List<RankListDto> getSeasonRankingList(String season, int rankingPageNum);
    List<RankListDto> getKeywordRankingList(String season, String keyword, int rankingPageNum);

}

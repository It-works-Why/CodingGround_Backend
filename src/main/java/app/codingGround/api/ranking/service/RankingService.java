package app.codingGround.api.ranking.service;


import app.codingGround.api.ranking.dto.response.RankListDto;
import app.codingGround.api.ranking.dto.response.SeasonListDto;
import app.codingGround.api.ranking.mapper.RankingMapper;
import app.codingGround.api.ranking.repository.RankingRepository;
import app.codingGround.api.user.dto.response.PageNumDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RankingService {


    private final RankingRepository rankingRepository;

    private final RankingMapper rankingMapper;

    public List<RankListDto> getRankingList(int rankingPageNum) {
        return rankingMapper.getRankingList(rankingPageNum);
    };

    public List<SeasonListDto> getSeasonList(){
        return rankingMapper.getSeasonList();
    }
    public PageNumDto getTotalPage(){
        return rankingMapper.getTotalPage();
    }
    public PageNumDto getSeasonTotalPage(){
        return rankingMapper.getSeasonTotalPage();
    }
    public PageNumDto getKeywordTotalPage(){
        return rankingMapper.getKeywordTotalPage();
    }

    public List<RankListDto> getSeasonRankingList(String season, int rankingPageNum) {
        return rankingMapper.getSeasonRankingList(season, rankingPageNum);
    };
    public List<RankListDto> getKeywordRankingList(String season ,String keyword,int rankingPageNum) {
        return rankingMapper.getKeywordRankingList(season,keyword, rankingPageNum);
    };


}
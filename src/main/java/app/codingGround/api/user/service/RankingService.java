package app.codingGround.api.user.service;


import app.codingGround.api.entity.Season;
import app.codingGround.api.user.dto.response.RankListDto;
import app.codingGround.api.user.mapper.RankingMapper;
import app.codingGround.api.user.repository.RankingRepository;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RankingService {


    private final RankingRepository rankingRepository;

    private final RankingMapper rankingMapper;

    public List<RankListDto> getRankingList() {
        return rankingMapper.getRankingList();
    };


}
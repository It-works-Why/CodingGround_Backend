package app.codingGround.api.ranking.repository;

import app.codingGround.api.entity.Rank;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RankingRepository extends JpaRepository<Rank, Long> {

    Rank findByRankNum(long rankNum);

}

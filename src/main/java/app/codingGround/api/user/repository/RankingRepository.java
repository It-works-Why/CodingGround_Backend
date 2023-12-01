package app.codingGround.api.user.repository;

import app.codingGround.api.entity.Rank;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RankingRepository extends JpaRepository<Rank, Long> {

    Rank findByRankNum(long rankNum);

}

package app.codingGround.api.user.repository;

import app.codingGround.api.entity.Season;
import app.codingGround.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RankingRepository extends JpaRepository<Season, Long> {



}

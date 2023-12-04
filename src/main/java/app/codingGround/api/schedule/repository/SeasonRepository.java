package app.codingGround.api.schedule.repository;

import app.codingGround.api.entity.Season;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeasonRepository extends JpaRepository<Season, Long> {

    Season findBySeasonNum(Long seasonNum);

    Season findFirstByOrderBySeasonNumDesc();
}

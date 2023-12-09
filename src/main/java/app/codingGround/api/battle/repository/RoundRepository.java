package app.codingGround.api.battle.repository;

import app.codingGround.api.entity.Game;
import app.codingGround.api.entity.Round;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoundRepository extends JpaRepository<Round, Long> {

    Round findByGameNumAndRound(Game gameNum, int round);
}

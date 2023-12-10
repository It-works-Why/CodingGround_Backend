package app.codingGround.api.battle.repository;

import app.codingGround.api.entity.Round;
import app.codingGround.api.entity.RoundRecord;
import app.codingGround.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoundRecordRepository extends JpaRepository<RoundRecord, Long> {
    RoundRecord findByRoundNumAndUserNum(Round round, User user);

    List<RoundRecord> findAllByRoundNum(Round round);
}

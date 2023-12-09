package app.codingGround.api.battle.repository;

import app.codingGround.api.entity.GameRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRecordRepository extends JpaRepository<GameRecord, Long> {
}

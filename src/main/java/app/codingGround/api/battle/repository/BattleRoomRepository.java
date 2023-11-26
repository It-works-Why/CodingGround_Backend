package app.codingGround.api.battle.repository;

import app.codingGround.api.entity.BattleRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BattleRoomRepository extends JpaRepository<BattleRoom, Long> {


    BattleRoom findByRoomId(String roomId);

    BattleRoom findByGameStatusAndParticipantsLessThanEqual(String gameStatus, int maxParticipants);
}

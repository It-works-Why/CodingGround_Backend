package app.codingGround.api.battle.repository;

import app.codingGround.api.entity.BattleRoom;
import org.springframework.data.repository.CrudRepository;

public interface BattleRoomRepository extends CrudRepository<BattleRoom, Long> {


    BattleRoom findByRoomId(String roomId);

    BattleRoom findByGameStatusAndParticipantsLessThanEqual(String gameStatus, int maxParticipants);
}

package app.codingGround.api.battle.repository;

import app.codingGround.api.entity.GameRoom;
import org.springframework.data.repository.CrudRepository;

public interface BattleRoomRepository extends CrudRepository<GameRoom, Long> {


    GameRoom findByRoomId(String roomId);

    GameRoom findByGameStatusAndParticipantsLessThanEqual(String gameStatus, int maxParticipants);
}

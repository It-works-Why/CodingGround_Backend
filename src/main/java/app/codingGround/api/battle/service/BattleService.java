package app.codingGround.api.battle.service;

import app.codingGround.api.battle.repository.BattleRoomRepository;
import app.codingGround.api.entity.GameRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BattleService {

    private final BattleRoomRepository GameRoomRepository;

    @Transactional
    public String createGameRoom() {
        GameRoom emptyRoom = emptyRoom();
        if(emptyRoom == null){
            GameRoom GameRoom = new GameRoom();
            GameRoomRepository.save(GameRoom);

            return GameRoom.getGameId();
        }else{
            return emptyRoom.getGameId();
        }
    }

    public GameRoom emptyRoom() {
        return GameRoomRepository.findByGameStatusAndParticipantsLessThanEqual("대기중", 7);
    }

}




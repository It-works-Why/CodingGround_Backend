package app.codingGround.api.battle.service;

import java.util.List;
import java.util.UUID;
import app.codingGround.api.battle.repository.BattleRoomRepository;
import app.codingGround.api.entity.BattleRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BattleService {

    private final BattleRoomRepository battleRoomRepository;

    public String createBattleRoom() {
        BattleRoom emptyRoom = emptyRoom();
        if(emptyRoom == null){
            BattleRoom battleRoom = new BattleRoom();
            battleRoomRepository.save(battleRoom);

            return battleRoom.getRoomId();
        }else{
            return emptyRoom.getRoomId();
        }
    }

    public BattleRoom emptyRoom() {
        return battleRoomRepository.findByGameStatusAndParticipantsLessThanEqual("대기중", 7);
    }

}

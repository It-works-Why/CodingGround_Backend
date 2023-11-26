package app.codingGround.api.battle.controller;


import app.codingGround.api.battle.service.BattleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/battle")
public class BattleRestController {

    private final BattleService battleService;

    @PostMapping("/start")
    public ResponseEntity<String> startBattle() {
        // 게임방 생성 또는 빈 방 확인 로직
        String roomId = battleService.createBattleRoom();

        if (roomId != null) {
            return ResponseEntity.ok("Joined room: " + roomId);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Matching failed");
        }
    }

    @MessageMapping("/connectRoom")
    @SendTo("/topic/{roomId}")
    public String connectRoom(String roomId) {
        return roomId;
    }

}

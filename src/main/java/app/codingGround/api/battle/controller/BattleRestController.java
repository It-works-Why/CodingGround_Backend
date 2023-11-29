package app.codingGround.api.battle.controller;

import app.codingGround.api.battle.dto.request.ConnectGameInfo;
import app.codingGround.api.battle.service.BattleService;
import app.codingGround.api.entity.Language;
import app.codingGround.global.config.exception.CustomException;
import app.codingGround.global.config.exception.ErrorCode;
import app.codingGround.global.config.model.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/battle")
public class BattleRestController {

    private final BattleService battleService;
    @GetMapping("/get/language")
    public List<Language> getLanguage() {
        return battleService.getLanguage();
    }

    @PostMapping("/join/game")
    public ResponseEntity<ApiResponse<String>> startBattle(@RequestHeader("Authorization") String accessToken,@RequestBody ConnectGameInfo connectGameInfo) {
        // 게임방 생성 또는 빈 방 확인 로직
        String roomId = battleService.createGameRoom(connectGameInfo, accessToken);

        if (roomId != null) {
            return ResponseEntity.ok(new ApiResponse<>(roomId));
        } else {
            throw new CustomException("게임방 생성 실패", ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

}

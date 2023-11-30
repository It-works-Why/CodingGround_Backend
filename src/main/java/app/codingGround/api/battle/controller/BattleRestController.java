package app.codingGround.api.battle.controller;

import app.codingGround.api.battle.dto.request.ConnectGameInfo;
import app.codingGround.api.battle.dto.response.QueueInfoDto;
import app.codingGround.api.battle.service.BattleService;
import app.codingGround.api.entity.Language;
import app.codingGround.domain.common.dto.response.DefaultResultDto;
import app.codingGround.global.config.exception.CustomException;
import app.codingGround.global.config.exception.ErrorCode;
import app.codingGround.global.config.model.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public ResponseEntity<ApiResponse<QueueInfoDto>> tryGameConnect(@RequestHeader("Authorization") String accessToken, @RequestBody ConnectGameInfo connectGameInfo) {
        // 게임방 생성 또는 빈 방 확인 로직
        QueueInfoDto queueInfoDto = battleService.tryGameConnect(connectGameInfo, accessToken);

        if (queueInfoDto != null) {
            return ResponseEntity.ok(new ApiResponse<>(queueInfoDto));
        } else {
            throw new CustomException("게임방 생성 실패", ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/reconnect/game")
    public ResponseEntity<ApiResponse<DefaultResultDto>> tryGameReconnect(@RequestHeader("Authorization") String accessToken) {
        return ResponseEntity.ok(new ApiResponse<>(battleService.acceptReconnect(accessToken)));
    }

    @PostMapping("/disconnect/game")
    public ResponseEntity<ApiResponse<DefaultResultDto>> tryGameDisconnect(@RequestHeader("Authorization") String accessToken) {
        return ResponseEntity.ok(new ApiResponse<>(battleService.denyReconnect(accessToken)));
    }


}

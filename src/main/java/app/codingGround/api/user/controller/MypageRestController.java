package app.codingGround.api.user.controller;

import app.codingGround.api.user.dto.response.GameRecordDto;
import app.codingGround.api.user.dto.response.UserInfoDto;
import app.codingGround.api.user.service.UserService;
import app.codingGround.global.config.model.ApiResponse;
import app.codingGround.global.utils.JwtTokenProvider;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@Api(tags = "사용자 관련 API")
@RequestMapping("/api/mypage")
@RequiredArgsConstructor
public class MypageRestController {

   private final UserService userService;


    @GetMapping("/myinfo")
    public ResponseEntity<ApiResponse<UserInfoDto>> getMypageInfo(@RequestHeader("Authorization") String accessToken) {
        String userId = JwtTokenProvider.getUserId(accessToken);
        UserInfoDto userInfo = new UserInfoDto();
        userInfo.setUserInfo(userService.getUserInfo(userId));
        userInfo.setRanking(userService.getUserRankings(userId));
        userInfo.setGameBadge(userService.getUserBadge(userId));
        userInfo.setGameLanguage(userService.getUserGameLanguage(userId));
        userInfo.setGameInfoData(userService.getUserGameInfo(userId));

        return ResponseEntity.ok(new ApiResponse<>(userInfo));
    }

    @GetMapping("/gamerecord/{gamenum}")
    public ResponseEntity<ApiResponse<GameRecordDto>>getmyrecord(@PathVariable Long gamenum) {
        GameRecordDto gameRecord = new GameRecordDto();


        return ResponseEntity.ok(new ApiResponse<>(gameRecord));
    }

}

package app.codingGround.api.user.controller;

import app.codingGround.api.entity.User;
import app.codingGround.api.user.dto.response.UserInfoVo;
import app.codingGround.api.user.repository.UserRepository;
import app.codingGround.api.user.service.UserService;
import app.codingGround.domain.common.dto.response.DefaultResultDto;
import app.codingGround.global.config.model.ApiResponse;
import app.codingGround.global.utils.JwtTokenProvider;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<ApiResponse<UserInfoVo>> getMypageInfo(@RequestHeader("Authorization") String accessToken) {
        String userId = JwtTokenProvider.getUserId(accessToken);
        return ResponseEntity.ok(new ApiResponse<>(userService.getUserInfo(userId)));
    }

}

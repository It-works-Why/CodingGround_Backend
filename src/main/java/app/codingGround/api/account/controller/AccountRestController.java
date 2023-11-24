package app.codingGround.api.account.controller;

import app.codingGround.api.account.dto.request.UserLoginRequestDto;
import app.codingGround.api.account.dto.request.UserRegisterDto;
import app.codingGround.global.config.model.TokenInfo;
import app.codingGround.api.account.service.AccountService;
import app.codingGround.domain.common.dto.response.DefaultResultDto;
import app.codingGround.global.config.model.ApiResponse;
import app.codingGround.global.utils.SHA256Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/account")
public class AccountRestController {

    private final AccountService accountService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<DefaultResultDto>> register(@RequestBody @Validated UserRegisterDto userRegisterDto) {
        return ResponseEntity.ok(new ApiResponse<>(accountService.register(userRegisterDto)));
    }
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenInfo>> login(@RequestBody UserLoginRequestDto userLoginRequestDto) {
        String userId = userLoginRequestDto.getUserId();
        String password = userLoginRequestDto.getUserPassword();
        TokenInfo tokenInfo = accountService.login(userId, SHA256Util.encrypt(password));
        return ResponseEntity.ok(new ApiResponse<>(tokenInfo));
    }

    @GetMapping("/get/accessToken")
    public ResponseEntity<ApiResponse<String>> getAccessToken(@RequestHeader("Authorization") String refreshToken) {
        return ResponseEntity.ok(new ApiResponse<>(accountService.getAccessToken(refreshToken)));
    }

    @GetMapping("/check/token")
    public ResponseEntity<ApiResponse<DefaultResultDto>> checkToken(@RequestHeader("Authorization") String accessToken) {
        return ResponseEntity.ok(new ApiResponse<DefaultResultDto>(accountService.checkToken(accessToken)));
    }

}

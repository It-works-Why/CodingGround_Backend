package app.codingGround.api.account.controller;

import app.codingGround.api.account.dto.request.UserLoginRequestDto;
import app.codingGround.api.account.entitiy.TokenInfo;
import app.codingGround.api.account.service.AccountService;
import app.codingGround.domain.common.dto.response.DefaultResultDto;
import app.codingGround.global.config.model.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class AccountRestController {

    private final AccountService accountService;

    @GetMapping("/register")
    public ResponseEntity<ApiResponse<DefaultResultDto>> register() {
        return ResponseEntity.ok(new ApiResponse<>(accountService.register()));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenInfo>> login(@RequestBody UserLoginRequestDto userLoginRequestDto) {
        String userId = userLoginRequestDto.getUserId();
        String password = userLoginRequestDto.getPassword();
        TokenInfo tokenInfo = accountService.login(userId, password);
        return ResponseEntity.ok(new ApiResponse<>(tokenInfo));
    }

    @GetMapping("/check/token")
    public ResponseEntity<ApiResponse<DefaultResultDto>> checkToken() {
        return ResponseEntity.ok(new ApiResponse<DefaultResultDto>(accountService.checkToken()));
    }

}

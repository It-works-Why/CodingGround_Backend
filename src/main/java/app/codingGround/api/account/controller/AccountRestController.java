package app.codingGround.api.account.controller;

import app.codingGround.api.account.dto.CommunityListDto;
import app.codingGround.api.account.dto.CommunityRegisterDto;
import app.codingGround.api.account.dto.request.UserLoginRequestDto;
import app.codingGround.api.account.dto.request.UserRegisterDto;
import app.codingGround.api.account.dto.response.UserInfoFromToken;
import app.codingGround.api.account.service.CommunityService;
import app.codingGround.api.admin.dto.NoticeRegisterDto;
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

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/account")
public class AccountRestController {

    private final CommunityService communityService;
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
        return ResponseEntity.ok(new ApiResponse<>(accountService.checkToken(accessToken)));
    }

    @GetMapping("/userInfo")
    public ResponseEntity<ApiResponse<UserInfoFromToken>> getUserInfoFromToken(@RequestHeader("Authorization") String accessToken) {
        return ResponseEntity.ok(new ApiResponse<>(accountService.getUserInfo(accessToken)));
    }

    @GetMapping("/community/list")
    public List<CommunityListDto> findByPostNum() {
        return communityService.getCommunityList();
    }

    @PostMapping("/community/register")
    public ResponseEntity<ApiResponse<DefaultResultDto>> postCommunity
            (@RequestHeader("Authorization") String accessToken,
             @RequestBody @Validated CommunityRegisterDto communityRegisterDto) {
        return ResponseEntity.ok(new ApiResponse<>(communityService.postCommunity(accessToken, communityRegisterDto)));
    }

    @GetMapping("/community/detail/{postNum}")
    public CommunityListDto getCommunityDetail(@PathVariable Long postNum) {
        return communityService.getCommunityDetail(postNum);
    }

    @PatchMapping("/community/edit/{postNum}")
    public ResponseEntity<ApiResponse<DefaultResultDto>> editCommunity
            (@RequestBody @Validated CommunityRegisterDto communityRegisterDto,
             @PathVariable Long postNum) {
        return ResponseEntity.ok(new ApiResponse<>(communityService.editCommunity(communityRegisterDto, postNum)));
    }

    @DeleteMapping("/community/delete/{postNum}")
    public ResponseEntity<ApiResponse<DefaultResultDto>> deleteCommunity (@PathVariable Long postNum) {
        return ResponseEntity.ok(new ApiResponse<>(communityService.deleteCommunity(postNum)));
    }

}

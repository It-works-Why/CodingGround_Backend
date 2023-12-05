package app.codingGround.api.account.controller;

import app.codingGround.api.account.dto.request.UserLoginRequestDto;
import app.codingGround.api.account.dto.request.UserRegisterDto;
import app.codingGround.api.account.dto.response.EmailCertificationDto;
import app.codingGround.api.account.dto.response.UserInfoFromToken;
import app.codingGround.global.config.model.TokenInfo;
import app.codingGround.api.account.service.AccountService;
import app.codingGround.domain.common.dto.response.DefaultResultDto;
import app.codingGround.global.config.model.ApiResponse;
import app.codingGround.global.utils.SHA256Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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
        return ResponseEntity.ok(new ApiResponse<>(accountService.checkToken(accessToken)));
    }

    @GetMapping("/userInfo")
    public ResponseEntity<ApiResponse<UserInfoFromToken>> getUserInfoFromToken(@RequestHeader("Authorization") String accessToken) {
        return ResponseEntity.ok(new ApiResponse<>(accountService.getUserInfo(accessToken)));
    }

    @PostMapping("/send/email")
    public Map sendEmail(@RequestBody UserRegisterDto userRegisterDto) {

        Map map = new HashMap<>();
        EmailCertificationDto dto = accountService.getEmail(userRegisterDto);

        if (dto != null) {
            map.put("exist", "이미 존재하는 이메일입니다.");
        } else {
            Random random = new Random(); // 난수 생성을 위한 랜덤 클래스
            String key = ""; // 인증번호 담을 String key 변수 생성

            SimpleMailMessage message = new SimpleMailMessage(); // 이메일 제목, 내용 작업 메서드
            message.setTo(userRegisterDto.getUserEmail()); // 스크립트에서 보낸 메일을 받을 사용자 이메일 주소

            // 입력 키를 위한 난수 생성 코드
            for (int i = 0; i < 3; i++) {
                int index = random.nextInt(26) + 65;
                key += (char) index;
            }
            for (int i = 0; i < 6; i++) {
                int numIndex = random.nextInt(10);
                key += numIndex;
            }

            message.setSubject("Coding-Ground 이메일 인증을 완료해주세요!"); // 이메일 제목
            String mail = "\n          안녕하세요. Coding-Ground ⚙️ 입니다. \n\n ----------------------------------------------------------------------- \n\n";
            message.setText(mail + "            인증번호는 🌟 " + key + " 🌟 입니다."); // 이메일 내용

            try {
                accountService.sendEmail(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
            map.put("key", key);
            map.put("dto", dto);
        }
        return map;
    }

    @PostMapping("/check/userId")
    public int checkUserId(@RequestBody UserRegisterDto userRegisterDto) {
        String userId = userRegisterDto.getUserId();
        return accountService.checkUserId(userId);
    }

    @PostMapping("/check/userNickname")
    public int checkUserNickname(@RequestBody UserRegisterDto userRegisterDto) {
        String userNickname = userRegisterDto.getUserNickname();
        return accountService.checkUserNickname(userNickname);
    }
}

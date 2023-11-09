package app.codingGround.api.account.service;

import app.codingGround.api.account.entitiy.TokenInfo;
import app.codingGround.api.account.entitiy.User;
import app.codingGround.api.account.repository.AccountRepository;
import app.codingGround.domain.common.dto.response.DefaultResultDto;
import app.codingGround.global.utils.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public TokenInfo login(String userId, String password) {
        // 1. Login ID/PW 를 기반으로 Authentication 객체 생성
        // 이때 authentication 는 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userId, password);
        // 2. 실제 검증 (사용자 비밀번호 체크)이 이루어지는 부분
        // authenticate 매서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드가 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);

        return tokenInfo;
    }


    public DefaultResultDto checkToken() {
        return DefaultResultDto.builder()
                .message("")
                .success(true)
                .build();
    }
    @Transactional
    public DefaultResultDto register() {
        User user = new User();
        user.setUserId("hhun6840");
        user.setUserPassword("1234");
        user.setUserName("황정민");
        user.setUserEmail("hhun6840@naver.com");
        user.setUserAffiliation("메가존클라우드");
        user.setUserAffiliationDetail("교육생");
        user.setUserProfileImg("url");
        user.setUserRole("USER");

        accountRepository.save(user);
        return DefaultResultDto.builder().success(true).message("성공").build();
    }
}

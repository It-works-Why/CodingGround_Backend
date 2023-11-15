package app.codingGround.api.account.service;

import app.codingGround.api.account.dto.request.UserRegisterDto;
import app.codingGround.api.account.entitiy.TokenInfo;
import app.codingGround.api.account.entitiy.User;
import app.codingGround.api.account.repository.AccountRepository;
import app.codingGround.domain.common.dto.response.DefaultResultDto;
import app.codingGround.global.config.exception.CustomException;
import app.codingGround.global.config.exception.ErrorCode;
import app.codingGround.global.utils.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public TokenInfo login(String userId, String password) {
        try {
            // 1. Login ID/PW 를 기반으로 Authentication 객체 생성
            // 이때 authentication 는 인증 여부를 확인하는 authenticated 값이 false
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userId, password);
            // 2. 실제 검증 (사용자 비밀번호 체크)이 이루어지는 부분
            // authenticate 매서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드가 실행
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            // 3. 인증 정보를 기반으로 JWT 토큰 생성
            TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);

            return tokenInfo;
        } catch (BadCredentialsException e) {
            // 로그인 정보가 잘못된 경우 예외 처리
            // 예를 들어, 클라이언트에게 로그인 실패 메시지를 반환하거나 로그를 남길 수 있습니다.
            throw new CustomException("로그인 정보가 올바르지 않습니다.", ErrorCode.INVALID_INPUT_ACCOUNT_INFO);
        }
    }


    public DefaultResultDto checkToken() {
        return DefaultResultDto.builder()
                .message("")
                .success(true)
                .build();
    }
    @Transactional
    public DefaultResultDto register(UserRegisterDto userRegisterDto) {
            // 아이디, 이메일, 닉네임 중복 체크
            if (accountRepository.existsByUserId(userRegisterDto.getUserId())) {
                throw new CustomException("아이디가 이미 존재합니다.", ErrorCode.SIGN_UP_ID_DUPLICATE);
            }
            if (accountRepository.existsByUserEmail(userRegisterDto.getUserEmail())) {
                throw new CustomException("이메일이 이미 존재합니다.", ErrorCode.SIGN_UP_EMAIL_DUPLICATE);
            }
            if (accountRepository.existsByUserNickname(userRegisterDto.getUserNickname())) {
                throw new CustomException("닉네임이 이미 존재합니다.", ErrorCode.SIGN_UP_NICKNAME_DUPLICATE);
            }
            User user = new User();
            user.setUserId(userRegisterDto.getUserId());
            user.setUserPassword(userRegisterDto.getUserPassword());
            user.setUserNickname(userRegisterDto.getUserNickname());
            user.setUserEmail(userRegisterDto.getUserEmail());
            user.setUserAffiliation(userRegisterDto.getUserAffiliation());
            user.setUserAffiliationDetail(userRegisterDto.getUserAffiliationDetail());
            user.setUserProfileImg(userRegisterDto.getUserProfileImg());
            accountRepository.save(user);
        return DefaultResultDto.builder().success(true).message("성공").build();
    }
}

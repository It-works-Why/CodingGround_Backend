package app.codingGround.api.account.service;

import app.codingGround.api.account.dto.request.UserRegisterDto;
import app.codingGround.api.account.dto.response.EditUserInfoDto;
import app.codingGround.api.account.dto.response.EmailCertificationDto;
import app.codingGround.api.account.dto.response.UserInfoFromToken;
import app.codingGround.global.config.model.TokenInfo;
import app.codingGround.api.entity.User;
import app.codingGround.api.account.repository.AccountRepository;
import app.codingGround.domain.common.dto.response.DefaultResultDto;
import app.codingGround.global.config.exception.CustomException;
import app.codingGround.global.config.exception.ErrorCode;
import app.codingGround.global.utils.JwtTokenProvider;
import app.codingGround.global.utils.SHA256Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final JavaMailSender javaMailSender;

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
            throw new CustomException("로그인 정보가 올바르지 않습니다.", ErrorCode.INVALID_INPUT_ACCOUNT_INFO);
        }
    }


    public DefaultResultDto checkToken(String accessToken) {
        return DefaultResultDto.builder()
                .message(JwtTokenProvider.getUserId(accessToken))
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
            user.setUserPassword(SHA256Util.encrypt(userRegisterDto.getUserPassword()));
            user.setUserNickname(userRegisterDto.getUserNickname());
            user.setUserEmail(userRegisterDto.getUserEmail());
            user.setUserAffiliation(userRegisterDto.getUserAffiliation());
            user.setUserAffiliationDetail(userRegisterDto.getUserAffiliationDetail());
            user.setUserProfileImg(userRegisterDto.getUserProfileImg());
            accountRepository.save(user);

//            User user2 = accountRepository.findByUserEmail(userRegisterDto.getUserEmail());


        return DefaultResultDto.builder().success(true).message("성공").build();
    }

    public String getAccessToken(String refreshToken) {
        try {
            String userId = JwtTokenProvider.getUserId(refreshToken);
            Optional<User> user = accountRepository.findByUserId(userId);
            TokenInfo token = login(userId, user.get().getPassword());
            return token.getAccessToken();
        } catch (Exception e) {
            throw new CustomException("리프레쉬 토큰 오류",ErrorCode.NEED_LOGIN);
        }
    }

    public UserInfoFromToken getUserInfo(String accessToken) {
        String userId = JwtTokenProvider.getUserId(accessToken);
        Optional<User> user = accountRepository.findByUserId(userId);

        return UserInfoFromToken.builder()
                .userNickname(user.get().getUserNickname())
                .userRole(user.get().getUserRole())
                .userId(userId).build();
    }

    public EmailCertificationDto getEmail(UserRegisterDto userRegisterDto) {
        User user = accountRepository.findByUserEmail(userRegisterDto.getUserEmail());

        if (user == null) {
            return null;
        } else {
            EmailCertificationDto userDto = new EmailCertificationDto(user);
            return userDto;
        }
    }

    public int sendEmail(SimpleMailMessage message) {
        javaMailSender.send(message);
        return 1;
    }

    public int checkUserId(String userId) {
        Optional<User> user = accountRepository.findByUserId(userId);

        if (user.isEmpty()) {
            return 1;
        } else {
            return 0;
        }
    }

    public int checkUserNickname(String userNickname) {
        Optional<User> user = accountRepository.findByUserNickname(userNickname);

        if (user.isEmpty()) {
            return 1;
        } else {
            return 0;
        }
    }

    public String checkUserEmail(String userEmail) {
        User user = accountRepository.findByUserEmail(userEmail);

        if (user == null) {
            return "";
        } else {
            return user.getUserId();
        }
    }

    public EmailCertificationDto getEmailAndId(UserRegisterDto userRegisterDto) {
        User user = accountRepository.findByUserEmailAndUserId(userRegisterDto.getUserEmail(), userRegisterDto.getUserId());

        if (user == null) {
            return null;
        } else {
            EmailCertificationDto userDto = new EmailCertificationDto(user);
            return userDto;
        }
    }

    @Transactional
    public void updatePassword(String userEmail, String key) {
        User user = accountRepository.findByUserEmail(userEmail);
        user.setUserPassword(key);
        accountRepository.save(user);
//        log.info("here!!!! user" + user.toString());
    }

    public EditUserInfoDto getUseInfoDetail(String accessToken) {
        String userId = JwtTokenProvider.getUserId(accessToken);
        Optional<User> user = accountRepository.findByUserId(userId);
        EditUserInfoDto editUserInfoDto = new EditUserInfoDto(user.get());
        return editUserInfoDto;
    }

    @Transactional
    public int updateUserInfo(UserRegisterDto userRegisterDto) {
        User user = accountRepository.findByUserEmail(userRegisterDto.getUserEmail());

        if (userRegisterDto.getUserNickname() != null) {
            Optional<User> userNickname = accountRepository.findByUserNickname(userRegisterDto.getUserNickname());
            if (userNickname.isEmpty()) {
               user.setUserNickname(userRegisterDto.getUserNickname());
               user.setUserAffiliation(userRegisterDto.getUserAffiliation());
               user.setUserAffiliationDetail(userRegisterDto.getUserAffiliationDetail());
               accountRepository.save(user);
               return 0;
           } else {
               return 1;
           }
        } else {
            user.setUserAffiliation(userRegisterDto.getUserAffiliation());
            user.setUserAffiliationDetail(userRegisterDto.getUserAffiliationDetail());
            accountRepository.save(user);
            return 0;
        }
    }
}

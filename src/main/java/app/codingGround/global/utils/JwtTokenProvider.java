package app.codingGround.global.utils;

import app.codingGround.api.account.entitiy.TokenInfo;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {

    private final Key key;
    private static Key staticKey = null;

    @Autowired
    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.staticKey = Keys.hmacShaKeyFor(keyBytes);
    }

    // 유저 정보를 가지고 AccessToken, RefreshToken 을 생성하는 메서드
    public TokenInfo generateToken(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String userId = userDetails.getUsername(); // 이 부분에서 사용자의 ID나 식별자를 가져옵니다.
        // 권한 가져오기
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = System.currentTimeMillis();

        // Access Token 생성
//        Date accessTokenExpiresIn = new Date(now + 60000); // 1분 테스트용
        Date accessTokenExpiresIn = new Date(now + 86400000); // 1일
        String accessToken = Jwts.builder()
                .setSubject(userId) // 사용자의 ID를 Subject로 설정합니다.
                .claim("auth", authorities)
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        // Refresh Token 생성 (액세스 토큰 만료 시간을 기준으로 설정)
        Date refreshTokenExpiresIn = new Date(now + 2 * 86400000); // 2일
        String refreshToken = Jwts.builder()
                .setSubject(userId) // 사용자의 ID를 Subject로 설정합니다.
                .claim("auth", authorities)
                .setExpiration(refreshTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        // 토큰 검증
//        if (validateToken(accessToken)) {
//        } else {
//        }

        // 토큰의 클레임 확인
//        Claims claims = parseClaims(accessToken);


        return TokenInfo.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userRole(authorities)
                .build();
    }

    // JWT 토큰을 복호화하여 토큰에 들어있는 정보를 꺼내는 메서드
    public Authentication getAuthentication(String accessToken) {
        // 토큰 복호화
        Claims claims = parseClaims(accessToken);
        if (claims.get("auth") == null) {
            throw new RuntimeException("토큰에 권한 정보가 없습니다.");
        }
        String subject = claims.getSubject();
        if (subject == null) {
            throw new RuntimeException("토큰에 사용자 정보가 없습니다.");
        }
        // 클레임에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("auth").toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        // UserDetails 객체를 만들어서 Authentication 리턴
        UserDetails principal = new User(subject, "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }


    // 토큰 정보를 검증하는 메서드
    public boolean validateToken(String token) {
        try {
            token = token.replace("Bearer", "").trim();
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | io.jsonwebtoken.security.SignatureException | MalformedJwtException e) {
            log.error("잘못된 JWT 토큰", e);
        } catch (ExpiredJwtException e) {
            log.error("만료된 JWT 토큰", e);
        } catch (UnsupportedJwtException e) {
            log.error("지원되지 않는 JWT 토큰", e);
        } catch (IllegalArgumentException e) {
            log.error("JWT claims 문자열이 비어 있습니다.", e);
        }
        return false;
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
    private static Claims getClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(staticKey).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public static String getUserId(String accessToken) {
        try {
            accessToken = accessToken.replace("Bearer", "").trim();
            Claims claims = getClaims(accessToken);
            return claims.getSubject();
        } catch (ExpiredJwtException e) {
            e.printStackTrace();
            return e.getClaims().getSubject();
        } catch (JwtException e) {
            e.printStackTrace();
            return null;
        }
    }
}

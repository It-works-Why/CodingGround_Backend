package app.codingGround.global.filter;

import app.codingGround.global.config.exception.ErrorCode;
import app.codingGround.global.config.exception.ErrorResponse;
import app.codingGround.global.config.security.Endpoint;
import app.codingGround.global.utils.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String requestUri = httpRequest.getRequestURI();
        if (requestUri.startsWith("/ws")) {
            System.out.println("여기왔음 왔음");
            chain.doFilter(request, response); // WebSocket 요청이면 필터링 건너뛰기
            return;
        }
        // Endpoint 열거형 URL 체크
        boolean isPublicEndpoint = Arrays.stream(Endpoint.values())
                .anyMatch(endpoint -> requestUri.startsWith(endpoint.getUrl()));

        if (isPublicEndpoint) {
            chain.doFilter(request, response);
            return;
        }

        String token = resolveToken(httpRequest);
        if (token != null && jwtTokenProvider.validateToken(token)) {
            // 토큰이 유효할 경우 토큰에서 Authentication 객체를 가져와 SecurityContext에 저장
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);
        } else {
            if(token == null){
                httpResponse.setContentType("application/json; charset=UTF-8");
                httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.registerModule(new JavaTimeModule());
                String jsonResponse = objectMapper.writeValueAsString(ErrorResponse.of(ErrorCode.NEED_LOGIN));

                httpResponse.getWriter().write(jsonResponse);
            }else{
            // 토큰이 유효하지 않은 경우 클라이언트에게 메시지를 보내고 요청을 중단합니다.
            httpResponse.setContentType("application/json; charset=UTF-8");
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            String jsonResponse = objectMapper.writeValueAsString(ErrorResponse.of(ErrorCode.INVALID_TOKEN));

            httpResponse.getWriter().write(jsonResponse);
            }
        }
    }



    // Request Header 에서 토큰 정보 추출
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
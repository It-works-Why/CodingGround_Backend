

package app.codingGround.global.config.web;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 모든 엔드포인트에 대해 CORS를 허용하도록 설정
                .allowedOriginPatterns(
                        "http://k8s-backendgroup-faceca018f-1950602437.ca-central-1.elb.amazonaws.com",
                        "https://k8s-backendgroup-faceca018f-1950602437.ca-central-1.elb.amazonaws.com",
                        "http://api.mzc-codingground.click",
                        "https://api.mzc-codingground.click",
                        "http://www.mzc-codingground.click",
                        "https://www.mzc-codingground.click"
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE","PATCH", "get","post","put","delete","patch") // 허용할 HTTP 메소드 설정
                .allowedHeaders("*") // 모든 헤더 허용
                .allowCredentials(true) // 인증 정보 (쿠키, 인증 헤더 등) 전달 허용
                .maxAge(3600); // CORS preflight 요청 결과를 캐시하는 시간 설정
    }
}

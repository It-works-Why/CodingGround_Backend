

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
                .allowedOriginPatterns("http://bsdev16-codingground-frontend.s3-website.ca-central-1.amazonaws.com") // Vue.js 애플리케이션의 도메인을 여기에 패턴으로 추가
                .allowedMethods("GET", "POST", "PUT", "DELETE") // 허용할 HTTP 메소드 설정
                .allowedHeaders("*") // 모든 헤더 허용
                .allowCredentials(true) // 인증 정보 (쿠키, 인증 헤더 등) 전달 허용
                .maxAge(3600); // CORS preflight 요청 결과를 캐시하는 시간 설정
    }

//    private final ObjectMapper objectMapper;

//    @Bean
//    public FilterRegistrationBean<XssEscapeServletFilter> filterRegistrationBean() {
//        FilterRegistrationBean<XssEscapeServletFilter> filterRegistration = new FilterRegistrationBean<>();
//        XssEscapeServletFilter xssEscapeServletFilter = new XssEscapeServletFilter();
//
//        // Set up URL patterns to include
//        filterRegistration.setFilter(xssEscapeServletFilter);
//        filterRegistration.setOrder(1);
//        filterRegistration.addUrlPatterns("/*");
//
//        // Exclude /api/mypage/gamerecord/* from filter
//
//        return filterRegistration;
//    }
//    @Override
//    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
//         converters.add(jsonEscapeConverter());
//    }
//    @Bean
//    public MappingJackson2HttpMessageConverter jsonEscapeConverter() {
//        ObjectMapper copy = objectMapper.copy();
//        copy.getFactory().setCharacterEscapes(new HtmlCharacterEscapes());
//        return new MappingJackson2HttpMessageConverter();
//    }
}

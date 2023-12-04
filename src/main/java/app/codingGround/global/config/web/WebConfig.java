
// vue.js 를 사용하기때문에 lucy 사용하지않음



//package app.codingGround.global.config.web;
//
//import app.codingGround.global.config.xss.HtmlCharacterEscapes;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.navercorp.lucy.security.xss.servletfilter.XssEscapeServletFilter;
//import lombok.RequiredArgsConstructor;
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.converter.HttpMessageConverter;
//import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
//
//import java.util.Collections;
//import java.util.List;
//
//@Configuration
//@RequiredArgsConstructor
//public class WebConfig implements WebMvcConfigurer {
//
//    private final ObjectMapper objectMapper;
//
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
//}

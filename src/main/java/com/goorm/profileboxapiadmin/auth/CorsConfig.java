package com.goorm.profileboxapiadmin.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {
    // cors 개념 정확히 알기 - 프론트 로컬, 도메인 접근할 수 있도록 설정
    @Bean
    public CorsFilter corsFilter(){
        System.out.println("cors filter 실행");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        // 확인해 보기
        config.setAllowCredentials(true); // 내 서버가 응답을 할 때 json을 자바스크립트에서 처리할 수 있게 할지를 설정하는 것
//        config.addAllowedOrigin("*"); // 모든 ip에 응답을 허용하겠다.
        config.addAllowedOrigin("http://localhost:5173");
        config.addAllowedOrigin("https://devwww.profilehub.info");
        config.addAllowedOrigin("https://devuser.profilehub.info");
        config.addAllowedOrigin("https://devadmin.profilehub.info");
        config.addAllowedOrigin("https://www.profilehub.info");
        config.addAllowedOrigin("https://user.profilehub.info");
        config.addAllowedOrigin("https://admin.profilehub.info");
        config.addAllowedHeader("*"); // 모든 header에 응답을 허용하겠다.
        config.addAllowedMethod("*"); // 모든 post, get, put, delete, patch 요청을 허용하겠다.
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

}


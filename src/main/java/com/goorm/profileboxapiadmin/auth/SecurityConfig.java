package com.goorm.profileboxapiadmin.auth;


import com.goorm.profileboxapiadmin.service.MemberService;
import com.goorm.profileboxcomm.service.MemberRedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.CorsFilter;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CorsFilter corsFilter;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final MemberService memberService;
    private final MemberRedisService memberRedisService;
    private final JwtProvider jwtProvider;

    public AuthenticationManager authenticationManager() throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable(); // csrf
        // 면접에서 물어볼만한 질문: 인증방식 중에 하나를 선택햇 ㅓ jwt를 선택한건데 왜 세션을 사용하냐?. 왜 jwt를 사용했는지?
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(corsFilter)
                .addFilter(new JwtAuthenticationFilter(authenticationManager(), jwtProvider, memberRedisService))
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), memberService, jwtProvider, memberRedisService))
                .formLogin().disable()
                .httpBasic().disable()
                .authorizeHttpRequests()
                // hasRole이나 hasAnyRole은 "ROLE_" prefix를 붙여버림.
                .requestMatchers("v1/notice/admin/**").hasAnyAuthority("ADMIN", "PRODUCER")
                .requestMatchers("/v1/open/**").permitAll()
                //.requestMatchers("v1/notice/admin/**").hasAuthority("ADMIN")
                .anyRequest().permitAll();
        return http.build();
    }

}

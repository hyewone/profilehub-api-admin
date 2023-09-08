package com.goorm.profileboxapiauth.auth;


import com.goorm.profileboxapiauth.service.AuthService;
import com.goorm.profileboxcomm.auth.JwtProvider;
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
    private final AuthService authServie;
    private final JwtProvider jwtProvider;

    public AuthenticationManager authenticationManager() throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable(); // csrf
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(corsFilter)
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), authServie, jwtProvider))
                .formLogin().disable()
                .httpBasic().disable()
                .authorizeHttpRequests()
//                .requestMatchers("v1/notice/admin/**").hasAnyAuthority("ADMIN", "PRODUCER")
                .requestMatchers("/v1/open/**").permitAll()
                //.requestMatchers("v1/notice/admin/**").hasAuthority("ADMIN")
                .anyRequest().permitAll();
        return http.build();
    }

}

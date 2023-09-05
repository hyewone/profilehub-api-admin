package com.goorm.profileboxapiauth.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.goorm.profileboxapiauth.service.AuthService;
import com.goorm.profileboxapiauth.service.MemberService;
import com.goorm.profileboxcomm.entity.Member;
import com.goorm.profileboxcomm.enumeration.ProviderType;
import com.goorm.profileboxcomm.exception.ApiException;
import com.goorm.profileboxcomm.exception.ExceptionEnum;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    private AuthService authService;
    private JwtProvider jwtProvider;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, AuthService authService, JwtProvider jwtProvider) {

        super(authenticationManager);
        this.authService = authService;
        this.jwtProvider = jwtProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        String jwtToken = jwtProvider.getJwtAccessTokenFromHeader(request)
                .orElse("");

        if(jwtToken.equals("")){
            chain.doFilter(request, response);
            return;
        }

        String email = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(jwtToken).getClaim("email").asString();
        String provider = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(jwtToken).getClaim("providerType").asString();

        if(email != null){
            Member member = authService.getMemberByMemberEmailAndProviderType(email, ProviderType.valueOf(provider))
                    .orElseThrow(() -> new ApiException(ExceptionEnum.MEMBER_NOT_FOUND));
            Authentication authentication = new UsernamePasswordAuthenticationToken(member, member.getMemberEmail(), member.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);
        }

    }
}

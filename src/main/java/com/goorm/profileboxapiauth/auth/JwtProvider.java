package com.goorm.profileboxapiauth.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.goorm.profileboxcomm.entity.Member;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

@Component
public class JwtProvider {
    // JwtToken 생성
    public String createJwtAccessToken(Member member) {
        String jwtToken = JWT.create()
                .withSubject(member.getMemberEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME))
                .withClaim("email", member.getMemberEmail())
                .withClaim("id", member.getMemberId())
                .withClaim("providerType", member.getProviderType().toString())
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));
        return jwtToken;
    }

    // Request 헤더에서 JwtToken 가져오기
    public Optional<String> getJwtAccessTokenFromHeader(HttpServletRequest request) {

        String jwtHeader = request.getHeader(JwtProperties.HEADER_STRING);

        if (jwtHeader != null && jwtHeader.startsWith(JwtProperties.TOKEN_PREFIX)) {
            String jwtToken = request.getHeader(JwtProperties.HEADER_STRING).replace(JwtProperties.TOKEN_PREFIX, "");
            return Optional.ofNullable(jwtToken);
        }
        return Optional.empty();
    }
}

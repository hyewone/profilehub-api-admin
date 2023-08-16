package com.goorm.profileboxapiadmin.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtProvider {

    @Value("${server.reactive.session.cookie.domain}")
    private String domain;

    @Value("${server.reactive.session.cookie.secure}")
    private boolean isSecure;

    // JwtToken 생성
    public String createJwtAccessToken(PrincipalDetails principalDetails){
        String jwtToken = JWT.create()
                .withSubject(principalDetails.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME))
                .withClaim("email", principalDetails.getMemberEntity().getMemberEmail())
                .withClaim("id", principalDetails.getMemberEntity().getMemberId())
                .withClaim("username", principalDetails.getMemberEntity().getMemberName())
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));
        return jwtToken;
    }

    // Request 헤더에서 JwtToken 가져오기
    public String getJwtAccessTokenFromHeader(HttpServletRequest request){

        String jwtHeader = request.getHeader(JwtProperties.HEADER_STRING);
        String jwtToken = "";

        if(jwtHeader != null && jwtHeader.startsWith(JwtProperties.TOKEN_PREFIX)){
            jwtToken = request.getHeader(JwtProperties.HEADER_STRING).replace(JwtProperties.TOKEN_PREFIX,"");
        }
        System.out.println("jwtTokenjwtToken ::: " +  jwtToken);
        return jwtToken;
    }

    // Request 쿠키에서 JwtToken 가져오기
    public String getJwtAccessTokenFromCookie(HttpServletRequest request){
        String clientJwtToken = "";
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(JwtProperties.ACCESS_TOKEN_COOKIE)) {
                    clientJwtToken = cookie.getValue();
                    break;
                }
            }
        }
        return clientJwtToken;
    }

    // Response 에 세팅할 JwtToken 쿠키 생성
    public ResponseCookie createJwtAccessTokenCookie(String jwtToken){
        ResponseCookie cookie = ResponseCookie.from(JwtProperties.ACCESS_TOKEN_COOKIE, jwtToken)
                .domain(domain)
                .sameSite("None")
                .secure(isSecure)
                .path("/")
                .httpOnly(true)
                .maxAge(JwtProperties.EXPIRATION_TIME)
                .build();
        return cookie;
    }
}

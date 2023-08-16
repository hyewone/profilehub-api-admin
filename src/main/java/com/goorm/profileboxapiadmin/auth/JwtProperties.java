package com.goorm.profileboxapiadmin.auth;

public interface JwtProperties {
    String SECRET = "뭐로 설정해야될까?"; // 우리 서버만 알고 있는 비밀값
    int EXPIRATION_TIME = 1000*60*60*3; // 유효시간 3시간
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";
    String ACCESS_TOKEN_COOKIE = "profilehub_access_token";
}

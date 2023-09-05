package com.goorm.profileboxapiauth.auth;

public interface JwtProperties {
    String SECRET = "139847138749183";
    int EXPIRATION_TIME = 1000*60*60*3; // 유효시간 3시간
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";
    String ACCESS_TOKEN_COOKIE = "profilehub_access_token";
}

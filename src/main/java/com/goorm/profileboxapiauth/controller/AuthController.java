package com.goorm.profileboxapiauth.controller;

import com.goorm.profileboxapiauth.auth.JwtProvider;
import com.goorm.profileboxapiauth.service.AuthService;
import com.goorm.profileboxcomm.dto.member.response.SelectLoginMemberResponseDto;
import com.goorm.profileboxcomm.dto.member.response.SelectMemberResponseDto;
import com.goorm.profileboxcomm.entity.Member;
import com.goorm.profileboxcomm.enumeration.ProviderType;
import com.goorm.profileboxcomm.exception.ApiException;
import com.goorm.profileboxcomm.exception.ExceptionEnum;
import com.goorm.profileboxcomm.response.ApiResult;
import com.goorm.profileboxcomm.response.ApiResultType;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
public class AuthController {
    private final AuthService authService;
    private final JwtProvider jwtProvider;

    @Value("${custom.oauth.google-url}")
    private String googleUrl;
    @Value("${custom.oauth.kakao-url}")
    private String kakaoUrl;
    @Value("${custom.oauth.naver-url}")
    private String naverUrl;

    @PostMapping("/login")
    public ApiResult<Member> login(@RequestParam String memberEmail,
                                   @RequestParam String memberType,
                                   @RequestParam String providerType){
        Member member = authService.getMemberByMemberEmailAndProviderType(memberEmail, ProviderType.valueOf(providerType))
                .orElseGet(() -> {
                    return authService.addMember(Member.createMember(memberType, providerType, memberEmail));
                });

        String newJwtToken = jwtProvider.createJwtAccessToken(member);

        SelectLoginMemberResponseDto result = new SelectLoginMemberResponseDto(member, newJwtToken);
        return ApiResult.getResult(ApiResultType.SUCCESS, "로그인", result);
    }

    @GetMapping("/logout")
    public ApiResult<?> logout(@RequestParam String memberEmail,
                                   @RequestParam String providerType){
        return ApiResult.getResult(ApiResultType.SUCCESS, "로그아웃", null);
    }

    @GetMapping("/signup")
    public ApiResult<?> logout(@RequestParam String memberEmail,
                               @RequestParam String providerType,
                               @RequestParam String memberType,
                               @RequestParam String jwtToken){
        return ApiResult.getResult(ApiResultType.SUCCESS, "회원가입", null);
    }

}
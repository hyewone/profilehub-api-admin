package com.goorm.profileboxapiauth.controller;

import com.goorm.profileboxapiauth.service.AuthService;
import com.goorm.profileboxcomm.auth.JwtProvider;
import com.goorm.profileboxcomm.dto.auth.requset.LoginRequestDto;
import com.goorm.profileboxcomm.dto.member.response.SelectLoginMemberResponseDto;
import com.goorm.profileboxcomm.entity.Member;
import com.goorm.profileboxcomm.response.ApiResult;
import com.goorm.profileboxcomm.response.ApiResultType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Auth")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
public class AuthController {
    private final AuthService authService;
    private final JwtProvider jwtProvider;

    @Operation(summary = "로그인")
    @PostMapping("/login")
    public ApiResult<Member> login(@Valid @ModelAttribute LoginRequestDto dto){

        Member member = authService.getMemberByMemberEmailAndProviderType(dto.getMemberEmail(), dto.getProviderType())
                .orElseGet(() -> {
                    return authService.addMember(Member.createMember(dto.getMemberType(), dto.getProviderType(), dto.getMemberEmail()));
                });

        String newJwtToken = jwtProvider.createJwtAccessToken(member);
        SelectLoginMemberResponseDto result = new SelectLoginMemberResponseDto(member, newJwtToken);
        return ApiResult.getResult(ApiResultType.SUCCESS, "로그인", result);
    }

    @Operation(summary = "인증 요청")
    @GetMapping("/verify")
    public Member checkAuth(Authentication authentication){
        Member member = (Member) authentication.getPrincipal();
        return member;
    }

//    @GetMapping("/logout")
//    public ApiResult<?> logout(@RequestParam String memberEmail,
//                                   @RequestParam String providerType){
//        return ApiResult.getResult(ApiResultType.SUCCESS, "로그아웃", null);
//    }
//
//    @GetMapping("/signup")
//    public ApiResult<?> logout(@RequestParam String memberEmail,
//                               @RequestParam String providerType,
//                               @RequestParam String memberType,
//                               @RequestParam String jwtToken){
//        return ApiResult.getResult(ApiResultType.SUCCESS, "회원가입", null);
//    }

}
package com.goorm.profileboxapiadmin.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goorm.profileboxcomm.dto.member.response.SelectMemberResponseDto;
import com.goorm.profileboxcomm.dto.member.response.SelectrLoginMemberResponseDto;
import com.goorm.profileboxcomm.entity.Member;
import com.goorm.profileboxcomm.exception.ApiException;
import com.goorm.profileboxcomm.exception.ExceptionEnum;
import com.goorm.profileboxcomm.response.ApiResult;
import com.goorm.profileboxcomm.response.ApiResultType;
import com.goorm.profileboxcomm.service.MemberRedisService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;
    private MemberRedisService memberRedisService;

    private JwtProvider jwtProvider;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtProvider jwtProvider, MemberRedisService memberRedisService) {
        this.authenticationManager = authenticationManager;
        this.memberRedisService = memberRedisService;
        this.jwtProvider = jwtProvider;
        setFilterProcessesUrl("/v1/auth/member/login");
    }
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println("로그인 시도중");

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Member member = objectMapper.readValue(request.getInputStream(), Member.class);

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(member.getMemberEmail(), member.getMemberPassword());
            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            return authentication;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        System.out.println("로그인 성공");
        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();
        // Hash암호 방식.
        String jwtToken = jwtProvider.createJwtAccessToken(principalDetails);

        // Redis 저장
        memberRedisService.saveMemberToRedis(jwtToken, principalDetails.getMember());

        // response 설정
//        ResponseCookie cookie = jwtProvider.createJwtAccessTokenCookie(jwtToken);
//        response.addHeader("Set-Cookie", cookie.toString());

        response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + jwtToken);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        ObjectMapper objectMapper = new ObjectMapper();

        SelectrLoginMemberResponseDto loginResponseDto = new SelectrLoginMemberResponseDto(
                new SelectMemberResponseDto(principalDetails.getMember())
                , jwtToken);

        String resStr = objectMapper.writeValueAsString(ApiResult.getResult(ApiResultType.SUCCESS, "로그인 성공! jwt 토큰 발급 완료", loginResponseDto));
        response.getWriter().write(resStr);

        System.out.println("토큰 발급됨");
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        System.out.println("로그인 실패!");
        // response 설정
//        response.setContentType("application/json");
//        response.setCharacterEncoding("UTF-8");
//        ObjectMapper objectMapper = new ObjectMapper();
//        String resStr = objectMapper.writeValueAsString(ApiResult.getResult(ApiResultType.ERROR, "로그인 실패!", null, HttpStatus.UNAUTHORIZED));
//        response.getWriter().write(resStr);
        throw new ApiException(ExceptionEnum.LOGIN_FAILED);
    }
}

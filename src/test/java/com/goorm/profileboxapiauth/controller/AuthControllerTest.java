package com.goorm.profileboxapiauth.controller;

import com.goorm.profileboxcomm.auth.JwtProvider;
import com.goorm.profileboxapiauth.service.AuthService;
import com.goorm.profileboxcomm.entity.Member;
import com.goorm.profileboxcomm.enumeration.MemberType;
import com.goorm.profileboxcomm.enumeration.ProviderType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthControllerTest.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @MockBean
    private JwtProvider jwtProvider;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void testLogin() throws Exception {
        // Mock 데이터 설정
        Member mockMember = new Member();
        mockMember.setMemberId(Long.valueOf(1));
        mockMember.setMemberEmail("test@example.com");
        mockMember.setMemberType(MemberType.ACTOR);
        mockMember.setProviderType(ProviderType.GOOGLE);

        String mockJwtToken = "mocked-jwt-token";

        when(authService.getMemberByMemberEmailAndProviderType(anyString(), any()))
                .thenReturn(Optional.of(mockMember));

        when(jwtProvider.createJwtAccessToken(any(Member.class)))
                .thenReturn(mockJwtToken);

        // 테스트 요청
        mockMvc.perform(post("/v1/auth/login")
                        .param("memberEmail", "test@example.com")
                        .param("memberType", "ACTOR")
                        .param("providerType", "GOOGLE"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("로그인 성공했습니다."))
                .andExpect(jsonPath("$.data.memberInfo.memberId").value(1))
                .andExpect(jsonPath("$.data.memberInfo.memberEmail").value("test@example.com"))
                .andExpect(jsonPath("$.data.memberInfo.memberType").value("ACTOR"))
                .andExpect(jsonPath("$.data.memberInfo.providerType").value("GOOGLE"))
                .andExpect(jsonPath("$.data.jwtToken").value(mockJwtToken));

        when(authService.addMember(any(Member.class)))
                .thenAnswer(invocation -> {
                    Member mockMember2 = new Member();
                    mockMember2.setMemberId(Long.valueOf(2));
                    mockMember2.setMemberEmail("test2@example.com");
                    mockMember2.setMemberType(MemberType.ACTOR);
                    mockMember2.setProviderType(ProviderType.GOOGLE);
                    return mockMember2;
                });

        mockMvc.perform(post("/v1/auth/login")
                        .param("memberEmail", "test2@example.com")
                        .param("memberType", "ACTOR")
                        .param("providerType", "GOOGLE"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("로그인 성공했습니다."))
                .andExpect(jsonPath("$.data.memberInfo.memberId").value(2))
                .andExpect(jsonPath("$.data.memberInfo.memberEmail").value("test2@example.com"))
                .andExpect(jsonPath("$.data.memberInfo.memberType").value("ACTOR"))
                .andExpect(jsonPath("$.data.memberInfo.providerType").value("GOOGLE"))
                .andExpect(jsonPath("$.data.jwtToken").value(mockJwtToken));
    }

    @Test
    public void testLogout() throws Exception {

    }

    @Test
    public void testSignup() throws Exception {

    }
}

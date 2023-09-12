package com.goorm.profileboxapiauth.project.todoList.controller;

import com.goorm.profileboxcomm.auth.JwtProvider;
import com.goorm.profileboxapiauth.service.AuthService;
import com.goorm.profileboxcomm.entity.Member;
import com.goorm.profileboxcomm.enumeration.MemberType;
import com.goorm.profileboxcomm.enumeration.ProviderType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
    public void testLoginBasic() throws Exception {
        // given
        Member mockMember = new Member();
        mockMember.setMemberId(Long.valueOf(1));
        mockMember.setMemberEmail("test@example.com");
        mockMember.setMemberType(MemberType.ACTOR);
        mockMember.setProviderType(ProviderType.GOOGLE);

        String mockJwtToken = "mocked-jwt-token";


        // when
        when(authService.getMemberByMemberEmailAndProviderType(anyString(), any()))
                .thenReturn(Optional.of(mockMember));

        when(jwtProvider.createJwtAccessToken(any(Member.class)))
                .thenReturn(mockJwtToken);

        // then
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
    }

    @Test
    public void testLoginSignup() throws Exception {
        // given
        String mockJwtToken = "mocked-jwt-token";

        // when
        when(authService.getMemberByMemberEmailAndProviderType(anyString(), any()))
                .thenReturn(Optional.empty());

        when(authService.addMember(any(Member.class)))
                .thenAnswer(invocation -> {
                    Member newMember = new Member();
                    newMember.setMemberId(Long.valueOf(2));
                    newMember.setMemberEmail("test2@example.com");
                    newMember.setMemberType(MemberType.ACTOR);
                    newMember.setProviderType(ProviderType.GOOGLE);
                    return newMember;
                });

        when(jwtProvider.createJwtAccessToken(any(Member.class)))
                .thenReturn(mockJwtToken);

        // then
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
    public void testLoginBadRequestInvalidEmail() throws Exception {
        mockMvc.perform(post("/v1/auth/login")
                        .param("memberEmail", "test2")
                        .param("memberType", "ACTOR")
                        .param("providerType", "GOOGLE"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("ERROR"))
                .andExpect(jsonPath("$.statusCode").value(500));
    }

    @Test
    public void testLoginBadRequestInvalidMemberType() throws Exception {
        mockMvc.perform(post("/v1/auth/login")
                        .param("memberEmail", "test2@gmail.com")
                        .param("memberType", "ACTORR")
                        .param("providerType", "GOOGLE"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("ERROR"))
                .andExpect(jsonPath("$.statusCode").value(500));
    }

    @Test
    public void testLoginBadRequestInvalidProviderType() throws Exception {
        mockMvc.perform(post("/v1/auth/login")
                        .param("memberEmail", "test2@gmail.com")
                        .param("memberType", "ACTOR")
                        .param("providerType", "GOOGLEE"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("ERROR"))
                .andExpect(jsonPath("$.statusCode").value(500));
    }

    @Test
    @Disabled
    public void testLogout() throws Exception {

    }

    @Test
    @Disabled
    public void testSignup() throws Exception {

    }
}

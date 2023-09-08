package com.goorm.profileboxapiauth.project.todoList;

import com.goorm.profileboxcomm.auth.JwtProvider;
import com.goorm.profileboxapiauth.service.AuthService;
import com.goorm.profileboxcomm.entity.Member;
import com.goorm.profileboxcomm.enumeration.ProviderType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.when;

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
        Member mockMember = new Member();
        mockMember.setMemberId(1L);
        mockMember.setMemberEmail("test@example.com");
        mockMember.setProviderType(ProviderType.GOOGLE);

        String mockJwtToken = "mocked-jwt-token";

        when(authService.getMemberByMemberEmailAndProviderType(anyString(), any())).thenReturn(Optional.of(mockMember));
        when(jwtProvider.createJwtAccessToken(any())).thenReturn(mockJwtToken);

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("memberEmail", "test@example.com")
                        .param("providerType", ProviderType.GOOGLE.toString())
                        .param("jwtToken", "sample-jwt-token")
                        .header("Auth", "sample-auth-token"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testLogout() throws Exception {

    }

    @Test
    public void testSignup() throws Exception {

    }
}

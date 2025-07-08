package com.cineflex.api.Controller;

import com.cineflex.api.controller.AuthenticationAPI;
import com.cineflex.api.model.Account;
import com.cineflex.api.model.VerificationToken;
import com.cineflex.api.service.AccountDetailService;
import com.cineflex.api.service.AuthenticationService;
import com.cineflex.api.service.EmailService;
import com.cineflex.api.service.JsonService;
import com.cineflex.api.service.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class AuthenticationAPITest {

    private MockMvc mockMvc;
    private AuthenticationService authenticationService;
    private JsonService jsonService;
    private TokenService tokenService;
    private AccountDetailService accountDetailService;
    private EmailService emailService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        authenticationService = mock(AuthenticationService.class);
        jsonService = mock(JsonService.class);
        tokenService = mock(TokenService.class);
        accountDetailService = mock(AccountDetailService.class);
        emailService = mock(EmailService.class);

        AuthenticationAPI controller = new AuthenticationAPI(
                authenticationService,
                jsonService,
                tokenService,
                accountDetailService,
                emailService
        );

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void testLoginSuccess() throws Exception {
        String email = "test@example.com";
        String password = "123456";
        String token = "mocked-token";

        ObjectNode jsonBody = objectMapper.createObjectNode();
        jsonBody.put("email", email);
        jsonBody.put("password", password);

        when(jsonService.getOrNull(any(), eq("email"), eq(String.class))).thenReturn(email);
        when(jsonService.getOrNull(any(), eq("password"), eq(String.class))).thenReturn(password);
        when(authenticationService.login(email, password)).thenReturn(token);

        mockMvc.perform(post("/api/authentication/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody.toString()))
                .andExpect(status().isOk())
                .andExpect(content().string(token));
    }

    @Test
    public void testRegisterSuccess() throws Exception {
        String username = "testuser";
        String email = "test@example.com";
        String password = "123456";

        ObjectNode jsonBody = objectMapper.createObjectNode();
        jsonBody.put("username", username);
        jsonBody.put("email", email);
        jsonBody.put("password", password);

        Account account = Account.builder()
                .id(UUID.randomUUID())
                .username(username)
                .email(email)
                .role(0)
                .verify(false)
                .build();

        when(jsonService.getOrNull(any(), eq("email"), eq(String.class))).thenReturn(email);
        when(jsonService.getOrNull(any(), eq("password"), eq(String.class))).thenReturn(password);
        when(jsonService.getOrNull(any(), eq("username"), eq(String.class))).thenReturn(username);
        when(authenticationService.register(username, email, password)).thenReturn(account);

        mockMvc.perform(post("/api/authentication/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody.toString()))
                .andExpect(status().isAccepted())
                .andExpect(content().string(email));
    }

    @Test
    public void testGetProfileUnauthorized() throws Exception {
        when(authenticationService.getAccount()).thenReturn(null);

        mockMvc.perform(get("/api/authentication/profile"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testGetProfileSuccess() throws Exception {
        Account account = Account.builder()
                .id(UUID.randomUUID())
                .username("testuser")
                .email("test@example.com")
                .role(1)
                .verify(true)
                .build();

        when(authenticationService.getAccount()).thenReturn(account);

        mockMvc.perform(get("/api/authentication/profile"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.role").value(1));
    }

    @Test
    public void testGetRoleSuccess() throws Exception {
        Account account = Account.builder()
                .id(UUID.randomUUID())
                .username("testuser")
                .email("test@example.com")
                .role(1)
                .verify(true)
                .build();

        when(authenticationService.getAccount()).thenReturn(account);

        mockMvc.perform(get("/api/authentication/role"))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }

    @Test
    public void testGetRoleUnauthorized() throws Exception {
        when(authenticationService.getAccount()).thenReturn(null);

        mockMvc.perform(get("/api/authentication/role"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testSendVerificationEmail_Success() throws Exception {
        String email = "test@example.com";
        String token = "mock-token";
        String host = "localhost:8080";

        Account mockAccount = Account.builder()
                .id(UUID.randomUUID())
                .username("user")
                .email(email)
                .verify(false)
                .build();

        VerificationToken mockToken = VerificationToken.builder()
                .id(UUID.randomUUID())
                .token(token)
                .account(mockAccount.getId())
                .verified(false)
                .build();

        ObjectNode body = objectMapper.createObjectNode();
        body.put("email", email);

        when(jsonService.getOrNull(any(), eq("email"), eq(String.class))).thenReturn(email);
        when(authenticationService.fromEmail(email)).thenReturn(mockAccount);
        when(tokenService.getAvailableToken(mockAccount)).thenReturn(null);
        when(tokenService.createToken(mockAccount)).thenReturn(mockToken);
        doNothing().when(emailService).sendActivationEmail(eq(token), eq(email), eq(host));

        mockMvc.perform(post("/api/authentication/verify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("host", host)
                        .content(body.toString()))
                .andExpect(status().isNoContent());
    }


    @Test
    public void testSendVerificationEmail_EmailNull() throws Exception {
        ObjectNode body = objectMapper.createObjectNode();

        when(jsonService.getOrNull(any(), eq("email"), eq(String.class))).thenReturn(null);

        mockMvc.perform(post("/api/authentication/verify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("host", "localhost")
                        .content(body.toString()))
                .andExpect(status().isNotAcceptable());
    }

    @Test
    public void testSendVerificationEmail_EmailNotFound() throws Exception {
        String email = "notfound@example.com";

        ObjectNode body = objectMapper.createObjectNode();
        body.put("email", email);

        when(jsonService.getOrNull(any(), eq("email"), eq(String.class))).thenReturn(email);
        doThrow(new ResponseStatusException(org.springframework.http.HttpStatus.UNAUTHORIZED))
                .when(emailService).sendActivationEmail(any(), eq(email), any());

        mockMvc.perform(post("/api/authentication/verify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("host", "localhost")
                        .content(body.toString()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testSendVerificationEmail_AlreadyVerified() throws Exception {
        String email = "already@example.com";

        Account verifiedAccount = Account.builder()
                .id(UUID.randomUUID())
                .username("user")
                .email(email)
                .verify(true)
                .build();

        ObjectNode body = objectMapper.createObjectNode();
        body.put("email", email);

        when(jsonService.getOrNull(any(), eq("email"), eq(String.class))).thenReturn(email);
        when(authenticationService.fromEmail(email)).thenReturn(verifiedAccount);

        mockMvc.perform(post("/api/authentication/verify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("host", "localhost")
                        .content(body.toString()))
                .andExpect(status().isConflict());
    }


    @Test
    public void testVerifyToken_Success() throws Exception {
        String token = "valid-token";
        doNothing().when(tokenService).verifyToken(token);

        mockMvc.perform(get("/api/authentication/verify/{token}", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testVerifyToken_Invalid() throws Exception {
        String token = "invalid-token";

        doThrow(new ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST, "Invalid token"))
                .when(tokenService).verifyToken(token);

        mockMvc.perform(get("/api/authentication/verify/{token}", token))
                .andExpect(status().isBadRequest());
    }
}

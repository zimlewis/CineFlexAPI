package com.cineflex.api.Controller;

import com.cineflex.api.controller.AccountAPI;
import com.cineflex.api.model.Account;
import com.cineflex.api.model.Subscription;
import com.cineflex.api.service.AccountDetailService;
import com.cineflex.api.service.AuthenticationService;
import com.cineflex.api.service.OrderService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class AccountAPITest {

    private MockMvc mockMvc;
    private AccountDetailService accountDetailService;
    private OrderService orderService;
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        accountDetailService = mock(AccountDetailService.class);
        orderService = mock(OrderService.class);
        authenticationService = mock(AuthenticationService.class);

        AccountAPI accountAPI = new AccountAPI(accountDetailService, orderService, authenticationService);
        mockMvc = MockMvcBuilders.standaloneSetup(accountAPI).build();
    }

    @Test
    void testGetUserById() throws Exception {
        UUID userId = UUID.randomUUID();
        Account mockAccount = new Account();
        mockAccount.setId(userId);
        mockAccount.setUsername("testuser");
        mockAccount.setEmail("test@example.com");
        mockAccount.setVerify(false);
        mockAccount.setCreatedTime(LocalDateTime.of(2024, 1, 1, 10, 0));

        when(accountDetailService.getById(userId)).thenReturn(mockAccount);

        mockMvc.perform(get("/api/users/" + userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.verify").value(false))
                .andExpect(jsonPath("$.createdTime[0]").value(2024))
                .andExpect(jsonPath("$.createdTime[1]").value(1))
                .andExpect(jsonPath("$.createdTime[2]").value(1))
                .andExpect(jsonPath("$.createdTime[3]").value(10))
                .andExpect(jsonPath("$.createdTime[4]").value(0));
        ;
    }

    @Test
    void testGetCurrentUserSubscription_Success() throws Exception {
        UUID accountId = UUID.randomUUID();
        Account account = new Account();
        account.setId(accountId);

        Subscription subscription = Subscription.builder()
                .id(UUID.randomUUID())
                .account(accountId)
                .startTime(LocalDateTime.parse("2024-01-01T00:00:00"))
                .endTime(LocalDateTime.parse("2024-12-31T00:00:00"))
                .build();

        when(authenticationService.getAccount()).thenReturn(account);
        when(orderService.getUserSubscription(accountId)).thenReturn(subscription);

        mockMvc.perform(get("/api/users/subscription"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.account").value(accountId.toString()))
                .andExpect(jsonPath("$.startTime[0]").value(2024))
                .andExpect(jsonPath("$.startTime[1]").value(1))
                .andExpect(jsonPath("$.startTime[2]").value(1))
                .andExpect(jsonPath("$.startTime[3]").value(0))
                .andExpect(jsonPath("$.startTime[4]").value(0))
                .andExpect(jsonPath("$.endTime[0]").value(2024))
                .andExpect(jsonPath("$.endTime[1]").value(12))
                .andExpect(jsonPath("$.endTime[2]").value(31))
                .andExpect(jsonPath("$.endTime[3]").value(0))
                .andExpect(jsonPath("$.endTime[4]").value(0));

    }

    @Test
    void testGetCurrentUserSubscription_NoSubscription() throws Exception {
        UUID accountId = UUID.randomUUID();
        Account account = new Account();
        account.setId(accountId);

        when(authenticationService.getAccount()).thenReturn(account);
        when(orderService.getUserSubscription(accountId)).thenReturn(null);

        mockMvc.perform(get("/api/users/subscription"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetCurrentUserSubscription_Unauthorized() throws Exception {
        when(authenticationService.getAccount())
                .thenThrow(new ResponseStatusException(org.springframework.http.HttpStatus.UNAUTHORIZED));

        mockMvc.perform(get("/api/users/subscription"))
                .andExpect(status().isUnauthorized());
    }
}

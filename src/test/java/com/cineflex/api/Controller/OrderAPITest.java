package com.cineflex.api.Controller;

import com.cineflex.api.controller.OrderAPI;
import com.cineflex.api.dto.BankTransfer;
import com.cineflex.api.model.BillingDetail;
import com.cineflex.api.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class OrderAPITest {

    private MockMvc mockMvc;
    private OrderService orderService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        orderService = mock(OrderService.class);
        OrderAPI controller = new OrderAPI(orderService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void testGetOrderByIdSuccess() throws Exception {
        UUID id = UUID.randomUUID();
        BillingDetail detail = BillingDetail.builder()
                .id(id)
                .amount(10000.0)
                .createdTime(LocalDateTime.now())
                .transactionCode("GD12345678")
                .paid(false)
                .build();

        when(orderService.getOrderById(id)).thenReturn(detail);

        mockMvc.perform(get("/api/orders/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.amount").value(10000.0));
    }

    @Test
    public void testGetOrderByIdFail() throws Exception {
        UUID id = UUID.randomUUID();
        when(orderService.getOrderById(id)).thenThrow(
                new ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Order not found"));

        mockMvc.perform(get("/api/orders/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateOrderSuccess() throws Exception {
        UUID orderId = UUID.randomUUID();
        UUID accountId = UUID.randomUUID();
        UUID subscriptionId = UUID.randomUUID();

        BillingDetail detail = BillingDetail.builder()
                .id(orderId)
                .account(accountId)
                .subscription(subscriptionId)
                .amount(10000.0)
                .createdTime(LocalDateTime.of(2024, 1, 1, 10, 0))
                .transactionCode("GD12345678")
                .paid(false)
                .build();

        when(orderService.createOrder(10000.0)).thenReturn(detail);

        mockMvc.perform(post("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(orderId.toString()))
                .andExpect(jsonPath("$.account").value(accountId.toString()))
                .andExpect(jsonPath("$.subscription").value(subscriptionId.toString()))
                .andExpect(jsonPath("$.amount").value(10000.0))
                .andExpect(jsonPath("$.transactionCode").value("GD12345678"))
                .andExpect(jsonPath("$.paid").value(false))
                .andExpect(jsonPath("$.createdTime[0]").value(2024))
                .andExpect(jsonPath("$.createdTime[1]").value(1))
                .andExpect(jsonPath("$.createdTime[2]").value(1))
                .andExpect(jsonPath("$.createdTime[3]").value(10))
                .andExpect(jsonPath("$.createdTime[4]").value(0));
            ;
    }


    @Test
    public void testCreateOrderFail() throws Exception {
        when(orderService.createOrder(10000.0))
                .thenThrow(new ResponseStatusException(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR, "Failed"));

        mockMvc.perform(post("/api/orders"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testConfirmOrderSuccess() throws Exception {
        BankTransfer transfer = new BankTransfer();
        transfer.setCode("GD12345678");
        transfer.setTransferType("in");
        UUID id = UUID.randomUUID();
        transfer.setTransferAmount(10000.0);

        // Không throw exception thì coi là thành công
        doNothing().when(orderService).confirmPayment(any(BankTransfer.class));

        mockMvc.perform(post("/api/orders/confirm")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transfer)))
                .andExpect(status().isOk());
    }

    @Test
    public void testConfirmOrderFail() throws Exception {
        BankTransfer transfer = new BankTransfer();
        transfer.setCode("GD12345678");
        transfer.setTransferType("in");
        UUID id = UUID.randomUUID();
        transfer.setTransferAmount(10000.0);

        doThrow(new ResponseStatusException(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR, "Confirm failed"))
                .when(orderService).confirmPayment(any(BankTransfer.class));

        mockMvc.perform(post("/api/orders/confirm")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transfer)))
                .andExpect(status().isInternalServerError());
    }
}

package com.cineflex.api.Controller;

import com.cineflex.api.controller.HealthCheck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class HealthCheckAPITest {

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new HealthCheck()).build();
    }

    @Test
    void testPing() throws Exception {
        mockMvc.perform(get("/api/health/ping"))
                .andExpect(status().isOk())
                .andExpect(content().string("pong"));
    }
}

package com.idgovern.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class AdminControllerTest {

    private MockMvc mockMvc;
    private AdminController adminController;

    @BeforeEach
    void setUp() {
        // Initialize the controller and the MockMvc standalone setup
        adminController = new AdminController();
        mockMvc = MockMvcBuilders.standaloneSetup(adminController).build();
    }

    @Test
    @DisplayName("Should return 'admin' view when accessing index.page")
    void testIndexReturnsCorrectView() throws Exception {
        mockMvc.perform(get("/admin/index.page"))
                .andExpect(status().isOk()) // Asserts HTTP 200
                .andExpect(view().name("admin")); // Asserts the view name is "admin"
    }
}
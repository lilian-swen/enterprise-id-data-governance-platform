package com.idgovern.controller;

import com.idgovern.beans.PageQuery;
import com.idgovern.param.SearchLogParam;
import com.idgovern.service.SysLogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class SysLogControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SysLogService sysLogService;

    @InjectMocks
    private SysLogController sysLogController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(sysLogController).build();
    }

    @Test
    @DisplayName("Should render the log management JSP page")
    void testPage() throws Exception {
        mockMvc.perform(get("/sys/log/log.page"))
                .andExpect(status().isOk())
                .andExpect(view().name("log"));
    }

    @Test
    @DisplayName("Should successfully call recover service for a given log ID")
    void testRecover() throws Exception {
        int logId = 500;

        mockMvc.perform(get("/sys/log/recover.json")
                        .param("id", String.valueOf(logId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ret").value(true));

        // Verify the recovery logic was actually triggered in the service
        verify(sysLogService).recover(logId);
    }

    @Test
    @DisplayName("Should return paginated logs based on search criteria")
    void testSearchPage() throws Exception {
        // Setup Search parameters
        String operator = "admin";

        // We can mock the return value for the paginated search
        when(sysLogService.searchPageList(any(SearchLogParam.class), any(PageQuery.class)))
                .thenReturn(null); // Or return a PageResult object if preferred

        mockMvc.perform(get("/sys/log/page.json")
                        .param("operator", operator)
                        .param("pageNo", "1")
                        .param("pageSize", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ret").value(true));
    }
}
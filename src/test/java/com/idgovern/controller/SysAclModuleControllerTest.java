package com.idgovern.controller;

import com.idgovern.param.AclModuleParam;
import com.idgovern.service.SysAclModuleService;
import com.idgovern.service.SysTreeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class SysAclModuleControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SysAclModuleService sysAclModuleService;

    @Mock
    private SysTreeService sysTreeService;

    @InjectMocks
    private SysAclModuleController sysAclModuleController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(sysAclModuleController).build();
    }

    @Test
    @DisplayName("Should render the ACL module management JSP page")
    void testPage() throws Exception {
        mockMvc.perform(get("/sys/aclModule/acl.page"))
                .andExpect(status().isOk())
                .andExpect(view().name("acl"));
    }

    @Test
    @DisplayName("Should successfully create an ACL module and return success JSON")
    void testSaveAclModule_Success() throws Exception {
        mockMvc.perform(get("/sys/aclModule/save.json")
                        .param("name", "User Management")
                        .param("parentId", "0")
                        .param("seq", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ret").value(true));

        verify(sysAclModuleService, times(1)).save(any(AclModuleParam.class));
    }

    @Test
    @DisplayName("Should return the hierarchical ACL module tree")
    void testTree_Success() throws Exception {
        when(sysTreeService.aclModuleTree()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/sys/aclModule/tree.json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ret").value(true))
                .andExpect(jsonPath("$.data").exists());
    }

    @Test
    @DisplayName("Should handle module deletion failure due to sub-elements")
    void testDelete_Failure() throws Exception {
        // Simulate a conflict exception (e.g., module not empty)
        doThrow(new RuntimeException("Module is not empty")).when(sysAclModuleService).delete(anyInt());

        mockMvc.perform(get("/sys/aclModule/delete.json")
                        .param("id", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ret").value(false))
                .andExpect(jsonPath("$.msg").value("ACL module deletion failed"));

        verify(sysAclModuleService).delete(10);
    }
}
package com.idgovern.controller;

import com.idgovern.dto.PageQuery;
import com.idgovern.dto.PageResult;
import com.idgovern.model.SysUser;
import com.idgovern.param.UserParam;
import com.idgovern.service.SysRoleService;
import com.idgovern.service.SysTreeService;
import com.idgovern.service.SysUserService;
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
import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class SysUserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SysUserService sysUserService;

    @Mock
    private SysTreeService sysTreeService;

    @Mock
    private SysRoleService sysRoleService;

    @InjectMocks
    private SysUserController sysUserController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(sysUserController).build();
    }

    @Test
    @DisplayName("Should return noAuth view for unauthorized access")
    void testNoAuth() throws Exception {
        mockMvc.perform(get("/sys/user/noAuth.page"))
                .andExpect(status().isOk())
                .andExpect(view().name("noAuth"));
    }

    @Test
    @DisplayName("Should successfully save a user and return success JSON")
    void testSaveUser() throws Exception {
        mockMvc.perform(get("/sys/user/save.json")
                        .param("username", "lilian.s")
                        .param("mail", "lilian@idgovern.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ret").value(true));

        // Verify that the service was actually called once
        verify(sysUserService).save(any(UserParam.class));
    }

    @Test
    @DisplayName("Should return paginated users using PageResult builder")
    void testPage() throws Exception {
        // 1. Arrange: Use the @Builder from your PageResult class
        PageResult<SysUser> mockResult = PageResult.<SysUser>builder()
                .data(Collections.singletonList(new SysUser())) // Default empty list from bean
                .total(1) // Represents total records regardless of page size
                .build();

        // Mocking the service to return our builder-generated result
        when(sysUserService.getPageByDeptId(eq(1), any(PageQuery.class)))
                .thenReturn(mockResult);

        // 2. Act & Assert
        mockMvc.perform(get("/sys/user/page.json")
                        .param("deptId", "1")
                        .param("pageNo", "1")
                        .param("pageSize", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ret").value(true))
                .andExpect(jsonPath("$.data.total").value(1))
                .andExpect(jsonPath("$.data.data").isArray());
    }

    @Test
    @DisplayName("Should return ACL tree and role list for a user")
    void testAcls() throws Exception {
        int userId = 123;
        when(sysTreeService.userAclTree(userId)).thenReturn(new ArrayList<>());
        when(sysRoleService.getRoleListByUserId(userId)).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/sys/user/acls.json")
                        .param("userId", String.valueOf(userId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ret").value(true))
                .andExpect(jsonPath("$.data.acls").exists())
                .andExpect(jsonPath("$.data.roles").exists());
    }
}
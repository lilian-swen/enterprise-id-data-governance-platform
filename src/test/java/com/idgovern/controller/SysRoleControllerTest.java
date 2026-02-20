package com.idgovern.controller;

import com.idgovern.model.SysUser;
import com.idgovern.param.RoleParam;
import com.idgovern.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class SysRoleControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SysRoleService sysRoleService;
    @Mock
    private SysTreeService sysTreeService;
    @Mock
    private SysRoleAclService sysRoleAclService;
    @Mock
    private SysRoleUserService sysRoleUserService;
    @Mock
    private SysUserService sysUserService;

    @InjectMocks
    private SysRoleController sysRoleController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(sysRoleController).build();
    }

    @Test
    @DisplayName("Should return role management page")
    void testPage() throws Exception {
        mockMvc.perform(get("/sys/role/role.page"))
                .andExpect(status().isOk())
                .andExpect(view().name("role"));
    }

    @Test
    @DisplayName("Should save role successfully")
    void testSaveRole() throws Exception {
        mockMvc.perform(get("/sys/role/save.json")
                        .param("name", "Admin")
                        .param("status", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ret").value(true));

        verify(sysRoleService).save(any(RoleParam.class));
    }

    @Test
    @DisplayName("Should change role ACLs given a comma-separated string")
    void testChangeAcls() throws Exception {
        mockMvc.perform(get("/sys/role/changeAcls.json")
                        .param("roleId", "1")
                        .param("aclIds", "1,2,3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ret").value(true));

        // Verify the string "1,2,3" was correctly converted to a list of Integers
        verify(sysRoleAclService).changeRoleAcls(eq(1), anyList());
    }

    @Test
    @DisplayName("Should separate users into selected and unselected lists")
    void testUsers() throws Exception {
        int roleId = 1;

        // Mock an assigned user
        SysUser selectedUser = new SysUser();
        selectedUser.setId(101);
        selectedUser.setStatus(1);

        // Mock an unassigned active user
        SysUser unselectedUser = new SysUser();
        unselectedUser.setId(102);
        unselectedUser.setStatus(1);

        when(sysRoleUserService.getListByRoleId(roleId)).thenReturn(Collections.singletonList(selectedUser));
        when(sysUserService.getAll()).thenReturn(Arrays.asList(selectedUser, unselectedUser));

        mockMvc.perform(get("/sys/role/users.json").param("roleId", String.valueOf(roleId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ret").value(true))
                .andExpect(jsonPath("$.data.selected[0].id").value(101))
                .andExpect(jsonPath("$.data.unselected[0].id").value(102));
    }
}
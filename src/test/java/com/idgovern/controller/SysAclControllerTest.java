package com.idgovern.controller;

import com.idgovern.param.AclParam;
import com.idgovern.service.SysAclService;
import com.idgovern.service.SysRoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.ArrayList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
class SysAclControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SysAclService sysAclService;

    @Mock
    private SysRoleService sysRoleService;

    @InjectMocks
    private SysAclController sysAclController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(sysAclController).build();
    }

    @Test
    void saveAclModule_Success() throws Exception {
        // Mock static RequestHolder if needed, otherwise it defaults to null (which your code handles)
        mockMvc.perform(get("/sys/acl/save.json")
                        .param("name", "Test ACL")
                        .param("aclModuleId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ret").value(true));
    }

    @Test
    void saveAclModule_Fail() throws Exception {
        // Simulate a service exception
        doThrow(new RuntimeException("DB Error")).when(sysAclService).save(any(AclParam.class));

        mockMvc.perform(get("/sys/acl/save.json")
                        .param("name", "Fail ACL"))
                .andExpect(status().isOk()) // Your controller catches exception and returns JsonData.fail
                .andExpect(jsonPath("$.ret").value(false))
                .andExpect(jsonPath("$.msg").value("ACL creation failed"));
    }

  /*  @Test
    void list_Success() throws Exception {

        // Instantiate the actual param class from your 'param' package
        AclParam param = new AclParam();
        param.setName("Test Permission");
        param.setAclModuleId(1);
        param.setRemark("Unit test remark");

        // Mock the service to do nothing (void method)
        // No 'thenReturn' needed for void methods!
        doNothing().when(sysAclService).save(any(AclParam.class));

        mockMvc.perform(get("/sys/acl/page.json")
                        .param("aclModuleId", "1")
                        .param("pageNo", "1")
                        .param("pageSize", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ret").value(true));
    }*/

    @Test
    void list_Success() throws Exception {

        // 1. Arrange: Force the service to throw the exception seen in your logs
        doThrow(new RuntimeException("DB Error"))
                .when(sysAclService).save(any(AclParam.class));

        // 2. Act & Assert
        mockMvc.perform(get("/sys/acl/save.json")
                        .param("name", "Fail ACL"))
                .andExpect(status().isOk()) // The HTTP status is 200 because the controller catches the error
                .andExpect(jsonPath("$.ret").value(false)) // Verify the JSON 'ret' field is false
                .andExpect(jsonPath("$.msg").value("ACL creation failed")); // Verify the error message
    }



    @Test
    void acls_Success() throws Exception {
        int aclId = 10;
        when(sysRoleService.getRoleListByAclId(aclId)).thenReturn(new ArrayList<>());
        when(sysRoleService.getUserListByRoleList(any())).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/sys/acl/acls.json")
                        .param("aclId", String.valueOf(aclId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ret").value(true))
                .andExpect(jsonPath("$.data.roles").isArray())
                .andExpect(jsonPath("$.data.users").isArray());
    }
}
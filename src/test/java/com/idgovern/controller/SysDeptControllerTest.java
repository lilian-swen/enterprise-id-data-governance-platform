package com.idgovern.controller;

import com.idgovern.dto.DeptLevelDto;
import com.idgovern.param.DeptParam;
import com.idgovern.service.SysDeptService;
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

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class SysDeptControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SysDeptService sysDeptService;

    @Mock
    private SysTreeService sysTreeService;

    @InjectMocks
    private SysDeptController sysDeptController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(sysDeptController).build();
    }

    @Test
    @DisplayName("Should render the department management page")
    void testPage() throws Exception {
        mockMvc.perform(get("/sys/dept/dept.page"))
                .andExpect(status().isOk())
                .andExpect(view().name("dept"));
    }

    @Test
    @DisplayName("Should successfully create a department and return success JSON")
    void testSaveDept_Success() throws Exception {
        mockMvc.perform(get("/sys/dept/save.json")
                        .param("name", "Engineering")
                        .param("parentId", "0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ret").value(true));

        verify(sysDeptService, times(1)).save(any(DeptParam.class));
    }

    @Test
    @DisplayName("Should return the full department tree structure")
    void testTree_Success() throws Exception {
        DeptLevelDto root = new DeptLevelDto();
        root.setName("Headquarters");

        when(sysTreeService.deptTree()).thenReturn(Collections.singletonList(root));

        mockMvc.perform(get("/sys/dept/tree.json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ret").value(true))
                .andExpect(jsonPath("$.data[0].name").value("Headquarters"));
    }

    @Test
    @DisplayName("Should handle department deletion successfully")
    void testDelete_Success() throws Exception {
        int deptId = 10;

        mockMvc.perform(get("/sys/dept/delete.json")
                        .param("id", String.valueOf(deptId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ret").value(true));

        verify(sysDeptService).delete(deptId);
    }

    @Test
    @DisplayName("Should return failure JSON when department deletion fails")
    void testDelete_Failure() throws Exception {
        doThrow(new RuntimeException("Node not empty")).when(sysDeptService).delete(anyInt());

        mockMvc.perform(get("/sys/dept/delete.json")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ret").value(false))
                .andExpect(jsonPath("$.msg").value("Department deletion failed"));
    }
}
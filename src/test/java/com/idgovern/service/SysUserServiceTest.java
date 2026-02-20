package com.idgovern.service;

import com.idgovern.beans.PageQuery;
import com.idgovern.beans.PageResult;
import com.idgovern.common.RequestHolder;
import com.idgovern.dao.SysUserMapper;
import com.idgovern.exception.ParamException;
import com.idgovern.model.SysUser;
import com.idgovern.param.UserParam;
import com.idgovern.util.IpUtil; // Added import
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SysUserServiceTest {

    @Mock
    private SysUserMapper sysUserMapper;

    @Mock
    private SysLogService sysLogService;

    @InjectMocks
    private SysUserService sysUserService;

    @Test
    @DisplayName("Should save user successfully when data is unique")
    void testSave_Success() {
        UserParam param = UserParam.builder()
                .username("lilian.s")
                .telephone("13800000000")
                .mail("lilian@idgovern.com")
                .deptId(1)
                .status(1)
                .build();

        when(sysUserMapper.countByTelephone(anyString(), any())).thenReturn(0);
        when(sysUserMapper.countByMail(anyString(), any())).thenReturn(0);

        SysUser operator = SysUser.builder().username("admin").build();

        // Fix for NullPointerException: Mock BOTH RequestHolder and IpUtil
        try (MockedStatic<RequestHolder> requestHolderMock = mockStatic(RequestHolder.class);
             MockedStatic<IpUtil> ipUtilMock = mockStatic(IpUtil.class)) {

            requestHolderMock.when(RequestHolder::getCurrentUser).thenReturn(operator);
            requestHolderMock.when(RequestHolder::getCurrentRequest).thenReturn(mock(HttpServletRequest.class));

            // Stub IpUtil to return a dummy IP to avoid the .split() error in IpUtil.java
            ipUtilMock.when(() -> IpUtil.getRemoteIp(any())).thenReturn("127.0.0.1");

            sysUserService.save(param);

            verify(sysUserMapper).insertSelective(any(SysUser.class));
            verify(sysLogService).saveUserLog(isNull(), any(SysUser.class));
        }
    }

    @Test
    @DisplayName("Should throw exception when telephone already exists")
    void testSave_DuplicateTelephone() {
        // Fix for UnnecessaryStubbingException:
        // Ensure this test only mocks what it actually hits before the exception is thrown.
        UserParam param = UserParam.builder()
                .telephone("13800000000")
                .username("lilian.s") // Added required fields to pass BeanValidator.check(param)
                .mail("test@test.com")
                .deptId(1)
                .status(1)
                .build();

        when(sysUserMapper.countByTelephone(eq("13800000000"), any())).thenReturn(1);

        assertThrows(ParamException.class, () -> sysUserService.save(param));
    }

    @Test
    @DisplayName("Should return paginated results using PageResult builder")
    void testGetPageByDeptId() {
        PageQuery query = new PageQuery();
        query.setPageNo(1);
        query.setPageSize(10);

        when(sysUserMapper.countByDeptId(1)).thenReturn(100);
        when(sysUserMapper.getPageByDeptId(eq(1), any(PageQuery.class)))
                .thenReturn(Collections.singletonList(new SysUser()));

        PageResult<SysUser> result = sysUserService.getPageByDeptId(1, query);

        assertEquals(100, result.getTotal());
        assertFalse(result.getData().isEmpty());
    }
}
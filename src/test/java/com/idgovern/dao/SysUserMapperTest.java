package com.idgovern.dao;

import com.idgovern.beans.PageQuery;
import com.idgovern.model.SysUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
// Add this to prevent Spring from trying to start your whole web layer
@ImportAutoConfiguration(exclude = {
        DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class
})
class SysUserMapperTest {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Test
    @DisplayName("Should insert a user and retrieve it by primary key")
    void testInsertAndSelect() {
        // Arrange
        SysUser user = SysUser.builder()
                .username("mapper.test")
                .telephone("13911112222")
                .mail("mapper@idgovern.com")
                .password("encrypted_pwd")
                .deptId(1)
                .status(1)
                .operateTime(new Date())
                .operator("system")
                .operateIp("127.0.0.1")
                .build();

        // Act
        sysUserMapper.insertSelective(user);
        SysUser retrieved = sysUserMapper.selectByPrimaryKey(user.getId());

        // Assert
        assertThat(retrieved).isNotNull();
        assertThat(retrieved.getUsername()).isEqualTo("mapper.test");
    }

    @Test
    @DisplayName("Should return correct count for email uniqueness check")
    void testCountByMail() {
        // Arrange
        SysUser user = SysUser.builder()
                .username("email.test")
                .mail("unique@test.com")
                .build();
        sysUserMapper.insertSelective(user);

        // Act
        int countExists = sysUserMapper.countByMail("unique@test.com", null);
        int countUpdateSameUser = sysUserMapper.countByMail("unique@test.com", user.getId());
        int countNewEmail = sysUserMapper.countByMail("new@test.com", null);

        // Assert
        assertThat(countExists).isEqualTo(1);
        assertThat(countUpdateSameUser).isEqualTo(0); // Should be 0 when excluding current user ID
        assertThat(countNewEmail).isEqualTo(0);
    }

    @Test
    @DisplayName("Should return paginated users for a department")
    void testGetPageByDeptId() {
        // Arrange: Insert multiple users for dept 1
        for (int i = 0; i < 5; i++) {
            sysUserMapper.insertSelective(SysUser.builder()
                    .username("user" + i)
                    .deptId(1)
                    .build());
        }

        PageQuery page = new PageQuery();
        page.setPageNo(1);
        page.setPageSize(2);

        // Act
        List<SysUser> result = sysUserMapper.getPageByDeptId(1, page);

        // Assert
        assertThat(result).hasSize(2); // Page size is 2
    }
}
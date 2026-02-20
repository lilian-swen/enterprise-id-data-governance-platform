package com.idgovern.controller;

import com.idgovern.model.SysUser;
import com.idgovern.service.SysUserService;
import com.idgovern.util.MD5Util;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SysUserService sysUserService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    @DisplayName("Login Success - Redirect to admin dashboard")
    void testLoginSuccess() throws Exception {
        String username = "admin";
        String password = "123";

        SysUser mockUser = new SysUser();
        mockUser.setUsername(username);
        mockUser.setPassword(MD5Util.encrypt(password)); // Must match encryption logic
        mockUser.setStatus(1);

        when(sysUserService.findByKeyword(username)).thenReturn(mockUser);

        mockMvc.perform(post("/login.page")
                        .param("username", username)
                        .param("password", password))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/index.page"));
    }

    @Test
    @DisplayName("Login Failure - User not found forwards back to signin.jsp")
    void testLoginUserNotFound() throws Exception {
        when(sysUserService.findByKeyword("nonexistent")).thenReturn(null);

        mockMvc.perform(post("/login.page")
                        .param("username", "nonexistent")
                        .param("password", "any"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("signin.jsp"))
                .andExpect(request().attribute("error", "User not found"));
    }

    @Test
    @DisplayName("Logout - Invalidate session and redirect to signin.jsp")
    void testLogout() throws Exception {
        MockHttpSession session = new MockHttpSession();
        SysUser user = new SysUser();
        user.setUsername("testUser");
        session.setAttribute(SysUser.SESSION_USER_KEY, user);

        mockMvc.perform(get("/logout.page").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("signin.jsp"));

        assertTrue(session.isInvalid(), "Session should be invalidated after logout");
    }
}
package com.idgovern.security;

import com.idgovern.model.SysUser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class LogMdcFilter extends OncePerRequestFilter {
    private static final String USER_ID = "userId";
    private static final String REQ_IP = "reqIp";
    private static final String SESSION_USER_KEY = "user"; // The key you use during login

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            // 1. Pull user from Session
            HttpSession session = request.getSession(false); // don't create a session if it doesn't exist
            String username = "Anonymous";

      /*      if (session != null && session.getAttribute(SESSION_USER_KEY) != null) {
                SysUser user = (SysUser) session.getAttribute(SESSION_USER_KEY);
                username = user.getUsername();
            }*/

            // Use the constant from SysUser model
            if (session != null && session.getAttribute(SysUser.SESSION_USER_KEY) != null) {
                SysUser user = (SysUser) session.getAttribute(SysUser.SESSION_USER_KEY);
                username = user.getUsername();
            }

            // 2. Identify the IP
            String ip = getRemoteIp(request);

            // 3. Populate MDC
            MDC.put(USER_ID, username);
            MDC.put(REQ_IP, ip);

            // 4. Continue with the filter chain
            filterChain.doFilter(request, response);
        } finally {
            // 5. MUST CLEAR MDC - Prevents info leaking between threads in the pool
            MDC.clear();
        }
    }

    private String getRemoteIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}

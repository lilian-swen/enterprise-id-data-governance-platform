package com.idgovern.security;

import com.google.common.collect.Sets;
import com.idgovern.common.ApplicationContextHelper;
import com.idgovern.common.JsonData;
import com.idgovern.common.RequestHolder;
import com.idgovern.model.SysUser;
import com.idgovern.service.SysCoreService;
import com.idgovern.util.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Set;


/**
 * Access Control Filter (ACL Filter)
 *
 * <p>
 * This security intercepts all incoming HTTP requests and performs
 * authentication and authorization validation before allowing
 * access to protected resources.
 * </p>
 *
 * <p>
 * Core Responsibilities:
 * <ul>
 *     <li>Validates whether the current user is logged in.</li>
 *     <li>Checks whether the requested URL requires permission validation.</li>
 *     <li>Verifies that the current user has access rights to the URL.</li>
 *     <li>Returns JSON error responses for API requests.</li>
 *     <li>Redirects to a no-authorization page for page requests.</li>
 * </ul>
 * </p>
 *
 * <p>
 * Security Design Notes:
 * <ul>
 *     <li>Implements URL-level access control (ACL).</li>
 *     <li>Uses ThreadLocal-based RequestHolder for current user context.</li>
 *     <li>Delegates permission verification to SysCoreService.</li>
 *     <li>Supports exclusion URLs for public or login-related pages.</li>
 * </ul>
 * </p>
 *
 * ------------------------------------------------------------------------
 * | Version | Date       | Author     | Description                     |
 * ------------------------------------------------------------------------
 * | 1.0     | 2016-03-19 | Lilian S.  | Initial creation                |
 * ------------------------------------------------------------------------
 *
 * @author Lilian S.
 * @version 1.0
 * @since 1.0
 */
@Slf4j
public class AclControlFilter implements Filter {

    /**
     * URLs that do NOT require authentication or authorization checks.
     */
    private static Set<String> exclusionUrlSet = Sets.newConcurrentHashSet();

    /**
     * Default page to redirect users when they have no authorization.
     */
    private final static String noAuthUrl = "/sys/user/noAuth.page";


    /**
     * Initializes the security.
     * Adds URLs that should be excluded from ACL validation.
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
//        String exclusionUrls = filterConfig.getInitParameter("exclusionUrls");
//        List<String> exclusionUrlList = Splitter.on(",").trimResults().omitEmptyStrings().splitToList(exclusionUrls);
//        exclusionUrlSet = Sets.newConcurrentHashSet(exclusionUrlList);
        // You can also configure exclusion URLs from web.xml if needed.
        exclusionUrlSet.add(noAuthUrl);
        exclusionUrlSet.add("/signin.jsp");
        exclusionUrlSet.add("/admin/index.page");
    }


    /**
     * Core filtering logic.
     *
     * Steps:
     * 1. Check whether the URL is excluded
     * 2. Check if the user is logged in
     * 3. Check if the user has permission to access the URL
     * 4. Continue security chain if authorized
     */
    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String servletPath = request.getServletPath();
        Map requestMap = request.getParameterMap();

        // 1️. Skip excluded URLs
        if (exclusionUrlSet.contains(servletPath)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        // 2. Check if user is logged in
        SysUser sysUser = RequestHolder.getCurrentUser();
        if (sysUser == null) {
            log.info("Unauthenticated access attempt to {}, parameters: {}",
                    servletPath, JsonMapper.obj2String(requestMap));
            noAuth(request, response);
            return;
        }

        // 3. Check URL-level permission
        SysCoreService sysCoreService = ApplicationContextHelper.popBean(SysCoreService.class);

        if (!sysCoreService.hasUrlAcl(servletPath)) {

            log.info("User {} attempted to access {}, but lacks permission. Parameters: {}",
                    JsonMapper.obj2String(sysUser),
                    servletPath,
                    JsonMapper.obj2String(requestMap));

            noAuth(request, response);
            return;
        }

        // 4. Authorized — continue request processing
        filterChain.doFilter(servletRequest, servletResponse);
        return;
    }


    /**
     * Handles unauthorized access.
     *
     * If the request is an AJAX request (.json),
     * return a JSON error message.
     *
     * Otherwise, redirect to the no-authorization page.
     */
    private void noAuth(HttpServletRequest request,
                        HttpServletResponse response) throws IOException {

        String servletPath = request.getServletPath();

        // Return JSON response for API calls
        if (servletPath.endsWith(".json")) {
            JsonData jsonData = JsonData.fail("You do not have permission to access this resource. Please contact the administrator.");
            response.setHeader("Content-Type", "application/json");
            response.getWriter().print(JsonMapper.obj2String(jsonData));
            return;
        } else {
            // Redirect for page requests
            clientRedirect(noAuthUrl, response);
            return;
        }
    }


    /**
     * Performs client-side redirection using JavaScript.
     *
     * The original URL is appended as a return parameter (ret),
     * so the system may redirect back after authorization if needed.
     */
    private void clientRedirect(String url, HttpServletResponse response) throws IOException{
        response.setHeader("Content-Type", "text/html");
        response.getWriter().print(
                "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" " +
                        "\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n" +
                        "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                        "<head>\n" +
                        "<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\"/>\n" +
                        "<title>Redirecting...</title>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "Redirecting, please wait...\n" +
                        "<script type=\"text/javascript\">\n" +
                        "window.location.href='" + url +
                        "?ret=' + encodeURIComponent(window.location.href);\n" +
                        "</script>\n" +
                        "</body>\n" +
                        "</html>\n"
        );
    }


    /**
     * Cleanup logic when security is destroyed.
     */
    @Override
    public void destroy() {
        // No resources to release
    }
}

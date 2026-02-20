package com.idgovern.common;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import java.util.Map;


/**
 * HTTP request interceptor for logging and thread-local cleanup.
 *
 * <p>
 * This interceptor captures request start time, optionally logs request parameters
 * and execution time, and ensures thread-local data (via {@link RequestHolder})
 * is properly cleaned up after request completion to prevent memory leaks.
 * </p>
 *
 * <p>
 * Business Rules:
 * <ul>
 *     <li>Record the request start time in {@code preHandle}.</li>
 *     <li>Perform optional logging of URL and request parameters.</li>
 *     <li>Clean up thread-local information after request processing.</li>
 *     <li>Execution time logging can be enabled by uncommenting logging statements.</li>
 * </ul>
 * </p>
 *
 * ------------------------------------------------------------------------
 * | Version | Date       | Author     | Description                     |
 * ------------------------------------------------------------------------
 * | 1.0     | 2016-03-06 | Lilian S.  | Initial creation                |
 * ------------------------------------------------------------------------
 *
 * @author Lilian S.
 * @version 1.0
 * @since 1.0
 */
@Slf4j
public class HttpInterceptor implements HandlerInterceptor {

    /**
     * Attribute key to store request start time.
     */
    private static final String START_TIME = "requestStartTime";


    /**
     * Pre-handle method executed before controller processing.
     *
     * <p>
     * Records the start time of the request and stores it in the request attributes.
     * </p>
     *
     * @param request  HTTP request
     * @param response HTTP response
     * @param handler  handler object
     * @return {@code true} to continue processing the request
     * @throws Exception if any error occurs
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String url = request.getRequestURI().toString();
        Map parameterMap = request.getParameterMap();
//        log.info("request start. url:{}, params:{}", url, JsonMapper.obj2String(parameterMap));
        long start = System.currentTimeMillis();
        request.setAttribute(START_TIME, start);
        return true;
    }


    /**
     * Post-handle method executed after controller processing but before view rendering.
     *
     * <p>
     * Can be used to log request execution time. Also ensures cleanup of thread-local data.
     * </p>
     *
     * @param request       HTTP request
     * @param response      HTTP response
     * @param handler       handler object
     * @param modelAndView  model and view object
     * @throws Exception if any error occurs
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//        String url = request.getRequestURI().toString();
//        long start = (Long) request.getAttribute(START_TIME);
//        long end = System.currentTimeMillis();
//        log.info("request finished. url:{}, cost:{}", url, end - start);
        removeThreadLocalInfo();
    }


    /**
     * After-completion method executed after view rendering.
     *
     * <p>
     * Logs total execution time and ensures thread-local cleanup.
     * </p>
     *
     * @param request  HTTP request
     * @param response HTTP response
     * @param handler  handler object
     * @param ex       exception thrown during processing, if any
     * @throws Exception if any error occurs
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String url = request.getRequestURI().toString();
        long start = (Long) request.getAttribute(START_TIME);
        long end = System.currentTimeMillis();
//        log.info("request completed. url:{}, cost:{}", url, end - start);

        removeThreadLocalInfo();
    }


    /**
     * Removes thread-local information stored in {@link RequestHolder}.
     *
     * <p>
     * This prevents memory leaks and ensures no residual request data
     * persists across different HTTP requests.
     * </p>
     */
    public void removeThreadLocalInfo() {
        RequestHolder.remove();;
    }
}

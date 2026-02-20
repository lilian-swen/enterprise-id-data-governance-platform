package com.idgovern.common;

import com.idgovern.exception.ParamException;
import com.idgovern.exception.PermissionException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;


/**
 * Global Exception Resolver for Spring MVC.
 *
 * <p>
 * Handles all uncaught exceptions thrown during request processing and returns
 * a standardized response depending on the request type (JSON vs page view).
 * </p>
 *
 * <p>
 * Business Rules:
 * <ul>
 *     <li>Requests ending with `.json` are considered AJAX/JSON requests.</li>
 *     <li>Requests ending with `.page` are considered full-page requests.</li>
 *     <li>Known exceptions ({@link PermissionException}, {@link ParamException}) return the specific error message.</li>
 *     <li>Unknown exceptions return a generic "System error" message, and the full exception is logged.</li>
 *     <li>All exceptions are logged with URL and stack trace for debugging.</li>
 * </ul>
 * </p>
 *
 * ------------------------------------------------------------------------
 * | Version | Date       | Author     | Description                     |
 * ------------------------------------------------------------------------
 * | 1.0     | 2016-03-06 | Lilian S.  | Initial creation                |
 * ------------------------------------------------------------------------
 *
 * @author Lilian
 * @version 1.0
 * @since 1.0
 */
@Slf4j
public class SpringExceptionResolver implements HandlerExceptionResolver {

    /**
     * Resolves exceptions thrown during request processing.
     *
     * @param request  the current HTTP request
     * @param response the current HTTP response
     * @param handler  the executed handler, or {@code null} if none chosen at the time of exception
     * @param ex       the exception that got thrown during handler execution
     * @return a {@link ModelAndView} object with standardized error response
     */
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        String url = request.getRequestURL().toString();
        ModelAndView mv;
        String defaultMsg = "System error";

        // Handle JSON requests (AJAX)
        if (url.endsWith(".json")) {
            if (ex instanceof PermissionException || ex instanceof ParamException) {
                JsonData result = JsonData.fail(ex.getMessage());
                mv = new ModelAndView("jsonView", result.toMap());
            } else {
                log.error("unknown json exception, url:" + url, ex);
                JsonData result = JsonData.fail(defaultMsg);
                mv = new ModelAndView("jsonView", result.toMap());
            }
        } else if (url.endsWith(".page")){ // Handle page requests
            log.error("unknown page exception, url:" + url, ex);
            JsonData result = JsonData.fail(defaultMsg);
            mv = new ModelAndView("exception", result.toMap());
        }
        // Handle other requests (fallback)
        else {
            log.error("unknow exception, url:" + url, ex);
            JsonData result = JsonData.fail(defaultMsg);
            mv = new ModelAndView("jsonView", result.toMap());
        }

        return mv;
    }
}

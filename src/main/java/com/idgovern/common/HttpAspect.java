package com.idgovern.common;


import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import java.util.Arrays;


/**
 * Aspect-Oriented Logging for the HTTP Controller Layer.
 *
 * <p>
 * Intercepts all public methods within the controller package to provide
 * automated request/response logging. It captures metadata such as URLs,
 * HTTP methods, client IP addresses, and method arguments.
 * </p>
 *
 * <p>
 * This aspect works in tandem with the ServiceLogAspect to provide full
 * traceability of a request as it moves from the web entry point into the
 * business logic layer.
 * </p>
 *
 * ------------------------------------------------------------------------
 * | Version | Date       | Author   | Description                        |
 * ------------------------------------------------------------------------
 * | 1.0     | 2026-02-22 | Lilian S.| Initial creation of HttpAspect     |
 * ------------------------------------------------------------------------
 *
 * @author Lilian S.
 * @version 1.0
 * @since 1.0
 */
@Aspect
@Component
@Slf4j
public class HttpAspect {
    /**
     * Pointcut that matches all public methods in the controller package.
     */
    @Pointcut("execution(public * com.idgovern.controller..*.*(..))")
    public void httpLog() {}


    /**
     * Log request details before the controller method executes.
     *
     * @param joinPoint provides access to the intercepted method metadata
     */
    @Before("httpLog()")
    public void doBefore(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) return;
        HttpServletRequest request = attributes.getRequest();

        // Log Request Metadata
        log.info(">>> [Request] URL: {} | Method: {} | IP: {} | Handler: {}.{}",
                request.getRequestURL().toString(),
                request.getMethod(),
                request.getRemoteAddr(),
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName());

        // Log Parameters (Useful for debugging frontend-backend mismatch)
        log.info(">>> [Args] {}", Arrays.toString(joinPoint.getArgs()));
    }


    /**
     * Log the response content after the controller method returns successfully.
     *
     * @param object the returned object from the controller method
     */
    @AfterReturning(returning = "object", pointcut = "httpLog()")
    public void doAfterReturning(Object object) {
        // Log the final JSON response sent to the client
        log.info("<<< [Response End] Data: {}", object);
    }
}

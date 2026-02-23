package com.idgovern.exception;

import com.idgovern.common.JsonData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * Global Exception Handler for centralized error management.
 *
 * <p>
 * This component intercepts exceptions thrown across the controller layer and
 * translates them into standardized {@link JsonData} responses. It ensures
 * that the client receives a meaningful error message rather than a raw stack trace.
 * </p>
 *
 * <p>
 * By decoupling error handling from business logic, this class enables a
 * cleaner "fail-fast" architecture where controllers and services can throw
 * exceptions without manual try-catch boilerplate.
 * </p>
 *
 * ------------------------------------------------------------------------
 * | Version | Date       | Author   | Description                        |
 * ------------------------------------------------------------------------
 * | 1.0     | 2026-02-22 | Lilian S.| Initial creation of GlobalExceptionHandler|
 * ------------------------------------------------------------------------
 *
 * @author Lilian S.
 * @version 1.0
 * @since 1.0
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Handle all unhandled exceptions.
     *
     * @param e the exception caught by Spring MVC
     * @return a formatted JsonData failure response
     */
    @ExceptionHandler(Exception.class)
    public JsonData handleException(Exception e) {
        // Detailed logging remains here while the response to the user is sanitized
        log.error("Global Exception caught: ", e);
        return JsonData.fail("System Error: " + e.getMessage());
    }

    /**
     * Handle static resource not found exceptions (e.g., missing favicon.ico).
     * In Spring Boot 3, requests for non-existent static resources throw a
     * {@link NoResourceFoundException}. This handler prevents these common
     * browser requests from flooding the error logs with full stack traces.
     *
     * @param e the resource not found exception
     */
    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleNoResourceFoundException(NoResourceFoundException e) {
        // Log as TRACE or ignore completely to keep production logs clean
        log.trace("Static resource not found: {}", e.getResourcePath());
    }
}

package com.idgovern.common;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;


/**
 * Service Layer Logging and Exception Handling Aspect.
 *
 * <p>
 * Provides centralized logging for all service layer operations. It automatically
 * records method entry (including arguments), execution duration, and exit status.
 * </p>
 *
 * <p>
 * This aspect also serves as a centralized exception logger, capturing stack traces
 * for unexpected errors before rethrowing them to ensure transactional integrity.
 * </p>
 *
 * ------------------------------------------------------------------------
 * | Version | Date       | Author    | Description                     |
 * ------------------------------------------------------------------------
 * | 1.0     | 2026-02-21 | Lilian S. | Initial creation of Log Aspect  |
 * ------------------------------------------------------------------------
 *
 * @author Lilian S.
 * @version 1.0
 * @since 1.0
 */
@Aspect
@Component
@Slf4j
public class ServiceLogAspect {

    /**
     * Pointcut that matches all methods in service package.
     * It tracks any class ending with "Service" in com.idgovern.service.
     */
    @Pointcut("execution(* com.idgovern.service..*Service.*(..))")
    public void serviceLayer() {}

    @Around("serviceLayer()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        Object[] args = joinPoint.getArgs();

        // 1. Log Entry (Replaces the manual log.info)
        log.info("==> [Service Start] {}.{}() | Args: {}", className, methodName, Arrays.toString(args));

        long start = System.currentTimeMillis();

        try {
            // 2. Execute the actual business logic (e.g., the save() code)
            Object result = joinPoint.proceed();

            long executionTime = System.currentTimeMillis() - start;

            // 3. Log Success
            log.info("<== [Service End] {}.{}() | Time: {}ms", className, methodName, executionTime);
            return result;

        } catch (Throwable e) {
            // 4. Automated Error Logging (Replaces the repetitive try-catch)
            long executionTime = System.currentTimeMillis() - start;
            log.error("!!! [Service Error] {}.{}() | Time: {}ms | Message: {}",
                    className, methodName, executionTime, e.getMessage(), e);

            // Critical: Rethrow so @Transactional can roll back the database!
            throw e;
        }
    }
}

package com.idgovern.common;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;


/**
 * Helper class for accessing Spring's ApplicationContext statically.
 *
 * <p>
 * This class provides convenient static methods to retrieve Spring beans
 * anywhere in the application without requiring explicit dependency injection.
 * It implements {@link ApplicationContextAware} to automatically capture
 * the Spring ApplicationContext during application startup.
 * </p>
 *
 * <p>
 * Business Rules:
 * <ul>
 *     <li>Ensure that the Spring context is initialized before calling {@code popBean} methods.</li>
 *     <li>Static access is primarily for utility or legacy code that cannot use dependency injection.</li>
 *     <li>Returning {@code null} if the ApplicationContext has not been set.</li>
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
@Component("applicationContextHelper")
public class ApplicationContextHelper implements ApplicationContextAware {

    /**
     * Spring ApplicationContext instance captured during startup.
     */
    private static ApplicationContext applicationContext;


    /**
     * Sets the ApplicationContext. This method is automatically called
     * by Spring during initialization.
     *
     * @param context the Spring ApplicationContext
     * @throws BeansException in case of context initialization errors
     */
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        applicationContext = context;
    }


    /**
     * Retrieve a Spring bean by its type from the ApplicationContext.
     *
     * @param clazz the class type of the bean
     * @param <T>   type parameter
     * @return the bean instance, or {@code null} if context is not initialized
     */
    public static <T> T popBean(Class<T> clazz) {
        if (applicationContext == null) {
            return null;
        }
        return applicationContext.getBean(clazz);
    }


    /**
     * Retrieve a Spring bean by its name and type from the ApplicationContext.
     *
     * @param name  the bean name
     * @param clazz the class type of the bean
     * @param <T>   type parameter
     * @return the bean instance, or {@code null} if context is not initialized
     */
    public static <T> T popBean(String name, Class<T> clazz) {
        if (applicationContext == null) {
            return null;
        }
        return applicationContext.getBean(name, clazz);
    }
}

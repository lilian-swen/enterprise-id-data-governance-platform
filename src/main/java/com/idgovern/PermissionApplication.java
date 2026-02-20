package com.idgovern;

import com.idgovern.common.HttpInterceptor;
import com.idgovern.security.AclControlFilter;
import com.idgovern.security.LoginFilter;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * ================================================================
 * Enterprise Identity & Data Governance Platform
 * ------------------------------------------------
 * Application Bootstrap Class
 *
 * Description:
 * Main entry point for the Permission (Access Control) module.
 * Responsible for:
 *   - Bootstrapping Spring Boot application
 *   - Registering servlet filters
 *   - Configuring HTTP interceptors
 *   - Scanning MyBatis mapper interfaces
 *
 * Architecture Responsibilities:
 *   1. Authentication Filter (LoginFilter)
 *   2. Authorization Filter (AclControlFilter)
 *   3. Global HTTP Interceptor (HttpInterceptor)
 *
 * Filter Execution Order:
 *   1 → LoginFilter
 *   2 → AclControlFilter
 *
 * @author Lilian S.
 * @version 1.0
 * @since 1.0
 * ================================================================
 */
@SpringBootApplication
@ComponentScan
@MapperScan(basePackages = {"com.idgovern.dao"})
public class PermissionApplication implements WebMvcConfigurer {

	/**
	 * Application entry point.
	 *
	 * @param args runtime arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(PermissionApplication.class, args);
	}


	/**
	 * Registers the ACL (Access Control List) authorization security.
	 *
	 * Responsibilities:
	 * - Performs permission validation
	 * - Validates user access rights to system/admin endpoints
	 * - Enforces fine-grained access control policies
	 *
	 * Applied to:
	 * - /sys/*
	 * - /admin/*
	 *
	 * Execution Order:
	 * - Order = 2 (Executed after LoginFilter)
	 *
	 * @return FilterRegistrationBean for ACL control
	 */
	@Bean
	public FilterRegistrationBean httpFilter() {
		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setFilter(new AclControlFilter());
		registration.addUrlPatterns("/sys/*","/admin/*");
		registration.setOrder(2);
		return registration;
	}


	/**
	 * Registers the Login authentication security.
	 *
	 * Responsibilities:
	 * - Validates user login session
	 * - Prevents unauthorized access
	 * - Ensures user identity is authenticated before ACL checks
	 *
	 * Applied to:
	 * - /sys/*
	 * - /admin/*
	 *
	 * Execution Order:
	 * - Order = 1 (Must execute before ACL validation)
	 *
	 * @return FilterRegistrationBean for login validation
	 */
	@Bean
	public FilterRegistrationBean loginFilter() {
		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setFilter(new LoginFilter());
		registration.addUrlPatterns("/sys/*","/admin/*");
		registration.setOrder(1);
		return registration;
	}


	/**
	 * Registers global HTTP interceptors.
	 *
	 * Responsibilities:
	 * - Logs incoming HTTP requests
	 * - Captures request lifecycle metrics
	 * - Provides centralized exception handling hook
	 * - Enables cross-cutting concerns (logging, tracing, auditing)
	 *
	 * Applied to:
	 * - All endpoints (/**)
	 *
	 * @param registry Interceptor registry
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new HttpInterceptor()).addPathPatterns("/**");
	}
}

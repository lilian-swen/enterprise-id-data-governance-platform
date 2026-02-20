package com.idgovern.config;

import com.alibaba.druid.support.jakarta.StatViewServlet;
import com.alibaba.druid.support.jakarta.WebStatFilter;
import jakarta.servlet.Filter;
import jakarta.servlet.Servlet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Druid Monitoring Configuration
 * Mapped to properties defined in application.yml
 */
@Configuration
public class DruidConfig {

    @Value("${druid.monitor.username}")
    private String username;

    @Value("${druid.monitor.password}")
    private String password;

    @Value("${druid.monitor.allow-ip}")
    private String allowIp;

    /**
     * Registers the Druid StatViewServlet for database monitoring.
     *
     * Responsibilities:
     * - Provides a web console at /druid/* to monitor database performance
     * - Configures IP whitelist and blacklist
     * - Sets login credentials for accessing the monitoring console
     * - Controls ability to reset statistics
     *
     * @return ServletRegistrationBean for Druid monitoring
     */
    @Bean
    public ServletRegistrationBean<Servlet> statViewServlet() {
        ServletRegistrationBean<Servlet> bean = new ServletRegistrationBean<>(new StatViewServlet(), "/druid/*");

        // Parameters from application.yml
        bean.addInitParameter("loginUsername", username);
        bean.addInitParameter("loginPassword", password);
        bean.addInitParameter("allow", allowIp);

        // Safety: Disable the "Reset All" button in the UI
        bean.addInitParameter("resetEnable", "false");

        return bean;
    }


    /**
     * Registers the Druid WebStatFilter to monitor SQL execution and web request performance.
     *
     * Responsibilities:
     * - Intercepts all web requests
     * - Tracks SQL execution statistics
     * - Excludes static resources and the Druid monitoring console from filtering
     *
     * @return FilterRegistrationBean for Druid WebStatFilter
     */
    @Bean
    public FilterRegistrationBean<Filter> statFilter() {
        FilterRegistrationBean<Filter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new WebStatFilter());

        // Monitor all web requests
        bean.addUrlPatterns("/*");

        // Exclude static resources from being logged in the SQL monitor
        bean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");

        return bean;
    }
}
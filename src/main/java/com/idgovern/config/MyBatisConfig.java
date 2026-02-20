package com.idgovern.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

/**
 * MyBatis Configuration
 * Configures the SQL Session Factory and maps the XML mapper locations.
 * We only need to create this class if we want to do something custom that application.yml can't handle:
 * Plugin Registration: If we are adding custom interceptors (like a manual pagination plugin or a data decryption plugin).
 * Multiple DataSources: If the project needs to connect to two different databases, we must manually define which SqlSessionFactory belongs to which database.
 * Complex Mapping Logic: If we need to programmatically configure how MyBatis behaves based on dynamic environment variables.
 *
 */
@Configuration
@MapperScan("com.idgovern.dao") // Scans DAO interfaces to create implementations
public class MyBatisConfig {

    @Value("${mybatis.mapper-locations}")
    private String mapperLocations;

    @Value("${mybatis.type-aliases-package}")
    private String typeAliasesPackage;

    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();

        // 1. Link to the Druid DataSource automatically provided by Spring
        factoryBean.setDataSource(dataSource);

        // 2. Set the package for Type Aliases (allows using 'User' instead of 'com.idgovern.model.User' in XML)
        factoryBean.setTypeAliasesPackage(typeAliasesPackage);

        // 3. Set the location of the XML mapper files
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        factoryBean.setMapperLocations(resolver.getResources(mapperLocations));

        // 4. (Optional) Configuration settings like CamelCase mapping
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setMapUnderscoreToCamelCase(true);
        factoryBean.setConfiguration(configuration);

        return factoryBean.getObject();
    }
}
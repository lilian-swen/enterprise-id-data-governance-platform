package com.idgovern.config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile({"dev", "test"}) // Only load this bean in dev or test environments
public class OpenApiConfig {

    @Value("@project.version@") // Pulls version from filtered pom.xml
    private String projectVersion;

    @Bean
    public OpenAPI idGovernOpenAPI() {
        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT"))) // Standard for modern RBAC
                .info(new Info()
                        .title("ID Governance Platform API")
                        .description("### Enterprise Identity & Data Governance\n" +
                                "Authorized access only. Use the `Authorize` button to inject your session token.")
                        .version(projectVersion)
                        .contact(new Contact()
                                .name("Lilian S.")
                                .email("lilian.swen@outlook.com"))
                        .license(new License().name("Apache 2.0").url("http://www.apache.org/licenses/LICENSE-2.0.html")))
                .externalDocs(new ExternalDocumentation()
                        .description("Governance Project Wiki")
                        .url("https://github.com/lilian-swen/id-governance-platform/wiki"));
    }
}

package com.ferreiradev.sistema_login.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfiguration {
    final String securitySchemeName = "bearerAuth";

    @Bean
    public OpenAPI defineOpenAPI() {
        Server localServer = new Server();
        localServer.setUrl("http://localhost:8080");
        localServer.setDescription("Servidor de desenvolvimento");

        Contact contact = new Contact()
                .name("Jamilly Ferreira")
                .url("https://github.com/jamillyferreira")
                .email("jamillyferreira.dev@gmail.com");

        Info information = new Info()
                .title("Sistema de Login API")
                .description("API responsável pelo gerenciamento de autenticação e usuários")
                .version("1.0")
                .contact(contact);

        SecurityScheme securityScheme = new SecurityScheme()
                .name(securitySchemeName)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .description("Insira o token JWT (sem o prefixo 'Bearer ')");

        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList(securitySchemeName);

        Components components = new Components()
                .addSecuritySchemes("bearerAuth", securityScheme);

        return new OpenAPI()
                .info(information)
                .addServersItem(localServer)
                .addSecurityItem(securityRequirement)
                .components(components);
    }
}

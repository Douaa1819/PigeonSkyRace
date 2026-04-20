package com.pigeonskyrace.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI openAPI() {
        Server devServer = new Server();
        devServer.setUrl("http://localhost:8082");
        devServer.setDescription("Development");

        Contact contact = new Contact();
        contact.setEmail("douaachemnane@gmail.com");

        Info info = new Info()
                .title("PigeonSkyRace API")
                .version("1.0")
                .contact(contact)
                .description("Pigeon racing competition management API. Use Authorize with Bearer JWT for protected routes.");

        return new OpenAPI()
                .info(info)
                .servers(List.of(devServer))
                .components(new Components().addSecuritySchemes("bearer-jwt",
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("JWT from POST /api/v1/auth/login")));
    }
}

package com.muvix.backend.config;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class OpenApiConfig {
    @Bean
    OpenAPI muvixOpenApi(){
        return new OpenAPI()
                .info(new Info()
                        .title("Muvix API")
                        .version("v1")
                        .description("REST API for Muvix authentication and marketplace publications. " +
                                "Authenticate with POST /api/v1/auth/login or /register, then authorize protected operations with the returned access token."))
                .components(new Components().addSecuritySchemes("bearerAuth",
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Use the accessToken returned by the authentication endpoints.")))
                .addTagsItem(new Tag().name("Authentication").description("Register, authenticate, refresh tokens, and read the current profile."))
                .addTagsItem(new Tag().name("Publications").description("Browse and manage individual and batch publications."))
                .addTagsItem(new Tag().name("Reservations").description("Reserve publications, cancel reservations, and confirm pickups with a PIN."))
                .addTagsItem(new Tag().name("Boxes").description("Group publications into sell, purchase, and full relocation boxes."));
    }
}

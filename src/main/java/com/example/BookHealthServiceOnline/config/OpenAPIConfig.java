package com.example.BookHealthServiceOnline.config;





import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {

    @Value("${bookOnlineHealthService.openapi.dev-url}")
    private String defaultUrl;

    @Bean


    public OpenAPI myOpenAPI() {

        String serverUrl = AppTenantContext.getServerUrl();
        if (serverUrl == null) {
            serverUrl = defaultUrl; // Fallback to default if not set
        }

        Server devServer = new Server();
        devServer.setUrl(serverUrl);
        devServer.setDescription("Server URL in Development environment");

        Info info = new Info()
                .title("BookHealthServiceOnline API")
                .version("1.0");

        return new OpenAPI()
                .info(info)
                .servers(List.of(devServer))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("bearer-jwt",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                        )
                )
                .addSecurityItem(new SecurityRequirement()
                        .addList("bearer-jwt"));
    }
}

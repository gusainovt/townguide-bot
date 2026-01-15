package io.project.townguidebot.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Townguide Bot API",
        version = "1.0.0",
        description = "Admin API for Townguide Telegram Bot"
    )
)
public class OpenApiConfig {

}

package io.project.townguidebot.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@ConfigurationProperties(prefix = "telegram.bot")
@Data
public class BotConfig {
    private String name;
    private String token;
    private Long owner;
}



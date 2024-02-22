package io.project.BorovskBot;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition
public class BorovskBotApplication {
	public static void main(String[] args) {

		SpringApplication.run(BorovskBotApplication.class, args);
	}

}

package io.project.townguidebot;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition
public class TownguideBotApplication {
	public static void main(String[] args) {

		SpringApplication.run(TownguideBotApplication.class, args);
	}

}

package io.project.townguidebot;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
		"spring.datasource.url=jdbc:h2:mem:townguide-context;DB_CLOSE_DELAY=-1;MODE=PostgreSQL",
		"spring.datasource.username=sa",
		"spring.datasource.password=",
		"spring.liquibase.enabled=false",
		"telegram.bot.name=test-bot",
		"telegram.bot.token=test-token",
		"telegram.bot.owner=1",
		"weather-forecast-service.url=http://localhost:0/weather?city={city}&apiKey={apiKey}",
		"weather-forecast-service.api-key=test-api-key",
		"cloudinary.cloud_name=test",
		"cloudinary.api_key=test",
		"cloudinary.api_secret=test",
		"jwt.secret=0123456789abcdef0123456789abcdef"
})
class TownguideBotApplicationTests {

	@Test
	void contextLoads() {
	}

}

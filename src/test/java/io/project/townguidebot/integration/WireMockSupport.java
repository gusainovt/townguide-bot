package io.project.townguidebot.integration;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import org.springframework.core.io.ClassPathResource;
import org.testcontainers.containers.GenericContainer;

final class WireMockSupport {

    private WireMockSupport() {
    }

    static void registerMappings(GenericContainer<?> wiremock, String... classpathResources) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://" + wiremock.getHost() + ":" + wiremock.getMappedPort(8080) + "/__admin/mappings");

        for (String resourcePath : classpathResources) {
            String json = readClasspathResource(resourcePath);
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();
            client.send(req, HttpResponse.BodyHandlers.discarding());
        }
    }

    private static String readClasspathResource(String classpathPath) throws IOException {
        ClassPathResource resource = new ClassPathResource(stripLeadingSlash(classpathPath));
        try (var in = resource.getInputStream()) {
            return new String(in.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    private static String stripLeadingSlash(String path) {
        return path.startsWith("/") ? path.substring(1) : path;
    }
}


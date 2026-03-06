package io.project.townguidebot.integration;

import io.project.townguidebot.exception.PhotoUploadException;
import io.project.townguidebot.exception.PlaceNotFoundException;
import io.project.townguidebot.exception.dto.ErrorResponse;
import io.project.townguidebot.service.CloudinaryService;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

class PhotoControllerIT extends AbstractIntegrationTest {

    @Autowired
    private org.springframework.boot.test.web.client.TestRestTemplate restTemplate;

    @MockBean
    private CloudinaryService cloudinaryService;

    @Test
    void savePhoto_ShouldReturn200_AndCallCloudinaryService() throws IOException {
        Long placeId = 10L;

        ResponseEntity<Void> resp = restTemplate.exchange(
                "/api/v1/photos/" + placeId,
                HttpMethod.POST,
                multipartBody("file", "hello".getBytes(), "photo.png", MediaType.IMAGE_PNG),
                Void.class
        );

        assertEquals(200, resp.getStatusCode().value());
        verify(cloudinaryService).uploadPhoto(any(), eq(placeId));
    }

    @Test
    void savePhoto_WhenPlaceMissing_ShouldReturn404WithErrorResponse() throws IOException {
        Long placeId = 999999L;
        doThrow(new PlaceNotFoundException("Place with id: " + placeId + " not found"))
                .when(cloudinaryService)
                .uploadPhoto(any(), eq(placeId));

        ResponseEntity<ErrorResponse> resp = restTemplate.exchange(
                "/api/v1/photos/" + placeId,
                HttpMethod.POST,
                multipartBody("file", "hello".getBytes(), "photo.png", MediaType.IMAGE_PNG),
                ErrorResponse.class
        );

        assertEquals(404, resp.getStatusCode().value());
        assertNotNull(resp.getBody());
        assertEquals("PLACE_NOT_FOUND", resp.getBody().getType().name());
        assertNotNull(resp.getBody().getMessage());
        assertTrue(resp.getBody().getMessage().contains(placeId.toString()));
        assertNotNull(resp.getBody().getTimestamp());
    }

    @Test
    void savePhoto_WhenUploadFailed_ShouldReturn400WithErrorResponse() throws IOException {
        Long placeId = 10L;
        doThrow(new PhotoUploadException("Failed to upload photo to Cloudinary"))
                .when(cloudinaryService)
                .uploadPhoto(any(), eq(placeId));

        ResponseEntity<ErrorResponse> resp = restTemplate.exchange(
                "/api/v1/photos/" + placeId,
                HttpMethod.POST,
                multipartBody("file", "hello".getBytes(), "photo.png", MediaType.IMAGE_PNG),
                ErrorResponse.class
        );

        assertEquals(400, resp.getStatusCode().value());
        assertNotNull(resp.getBody());
        assertEquals("FAIL_UPLOAD_PHOTO", resp.getBody().getType().name());
        assertNotNull(resp.getBody().getMessage());
        assertNotNull(resp.getBody().getTimestamp());
    }

    private static HttpEntity<MultiValueMap<String, Object>> multipartBody(
            String partName,
            byte[] bytes,
            String filename,
            MediaType mediaType
    ) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        ByteArrayResource file = new ByteArrayResource(bytes) {
            @Override
            public String getFilename() {
                return filename;
            }
        };

        HttpHeaders partHeaders = new HttpHeaders();
        partHeaders.setContentType(mediaType);
        HttpEntity<ByteArrayResource> part = new HttpEntity<>(file, partHeaders);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add(partName, part);

        return new HttpEntity<>(body, headers);
    }
}

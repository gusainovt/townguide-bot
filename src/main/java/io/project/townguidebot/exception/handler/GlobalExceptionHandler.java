package io.project.townguidebot.exception.handler;

import static io.project.townguidebot.model.dto.ExType.AD_NOT_FOUND;
import static io.project.townguidebot.model.dto.ExType.FAIL_UPLOAD_PHOTO;
import static io.project.townguidebot.model.dto.ExType.PHOTO_NOT_FOUND;
import static io.project.townguidebot.model.dto.ExType.PLACE_NOT_FOUND;
import static io.project.townguidebot.model.dto.ExType.STORY_NOT_FOUND;
import io.project.townguidebot.exception.AdNotFoundException;
import io.project.townguidebot.exception.PhotoNotFoundException;
import io.project.townguidebot.exception.PhotoUploadException;
import io.project.townguidebot.exception.PlaceNotFoundException;
import io.project.townguidebot.exception.StoryNotFoundException;
import io.project.townguidebot.model.dto.ErrorResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler  {

    @ExceptionHandler(AdNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ApiResponse(responseCode = "404", description = "Ad not found")
    public ResponseEntity<ErrorResponse> handleBusinessException(AdNotFoundException ex) {
        ErrorResponse errorResponse =  ErrorResponse.builder()
                .type(AD_NOT_FOUND)
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.of(Optional.of(errorResponse));
    }

    @ExceptionHandler(PhotoNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ApiResponse(responseCode = "404", description = "Photo not found")
    public ResponseEntity<ErrorResponse> handleBusinessException(PhotoNotFoundException ex) {
        ErrorResponse errorResponse =  ErrorResponse.builder()
                .type(PHOTO_NOT_FOUND)
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.of(Optional.of(errorResponse));
    }

    @ExceptionHandler(PlaceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ApiResponse(responseCode = "404", description = "Place not found")
    public ResponseEntity<ErrorResponse> handleBusinessException(PlaceNotFoundException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .type(PLACE_NOT_FOUND)
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.of(Optional.of(errorResponse));
    }

    @ExceptionHandler(StoryNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ApiResponse(responseCode = "404", description = "Place not found")
    public ResponseEntity<ErrorResponse> handleBusinessException(StoryNotFoundException ex) {
        ErrorResponse errorResponse =  ErrorResponse.builder()
                .type(STORY_NOT_FOUND)
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.of(Optional.of(errorResponse));
    }

    @ExceptionHandler(PhotoUploadException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ApiResponse(responseCode = "404", description = "Failed upload photo to Cloudinary")
    public ResponseEntity<ErrorResponse> handleBusinessException(PhotoUploadException ex) {
        ErrorResponse errorResponse =  ErrorResponse.builder()
                .type(FAIL_UPLOAD_PHOTO)
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.of(Optional.of(errorResponse));
    }

}

package io.project.townguidebot.dto;

import lombok.Data;
import org.springframework.http.HttpHeaders;


@Data
public class ImagePreviewDto {
    HttpHeaders headers;
    byte[] data;
}

package io.project.BorovskBot.model.dto;

import lombok.Data;
import org.springframework.http.HttpHeaders;


@Data
public class ImagePreviewDto {

    HttpHeaders headers;
    byte[] data;
}

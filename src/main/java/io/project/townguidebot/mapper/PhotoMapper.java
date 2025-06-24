package io.project.townguidebot.mapper;

import io.project.townguidebot.model.Photo;
import io.project.townguidebot.model.dto.ImagePreviewDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PhotoMapper {
    @Mapping(target = "headers", expression = "java(createHeaders(photo))")
    ImagePreviewDto photoToImagePreviewDto(Photo photo);

    default HttpHeaders createHeaders(Photo photo) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(photo.getMediaType()));
        headers.setContentLength(photo.getData().length);
        return headers;
    }

}

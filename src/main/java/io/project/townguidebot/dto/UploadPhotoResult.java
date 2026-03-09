package io.project.townguidebot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UploadPhotoResult {

  private String url;

  private Long fileSize;

  private String mediaType;

  private String publicId;
}

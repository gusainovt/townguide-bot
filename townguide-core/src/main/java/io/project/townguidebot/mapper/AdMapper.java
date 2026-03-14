package io.project.townguidebot.mapper;

import io.project.townguidebot.model.Ad;
import io.project.townguidebot.dto.AdDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AdMapper {
    AdDto toAdDto(Ad ad);
    @Mapping(target = "id", ignore = true)
    Ad ToAd(AdDto adDto);
    List<AdDto> toListAdsDto(List<Ad> ads);

}

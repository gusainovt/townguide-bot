package io.project.BorovskBot.mapper;

import io.project.BorovskBot.model.Ad;
import io.project.BorovskBot.model.dto.AdDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AdMapper {
    AdDto toAdDto(Ad ad);
    Ad ToAd(AdDto adDto);
    List<AdDto> toListAdsDto(List<Ad> ads);

}

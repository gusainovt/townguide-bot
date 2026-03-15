package io.project.townguidebot.service.impl;

import io.project.townguidebot.dto.UploadPhotoResult;
import io.project.townguidebot.dto.UploadedFile;
import io.project.townguidebot.dto.request.PlaceCreateRq;
import io.project.townguidebot.dto.response.PlaceCreateRs;
import io.project.townguidebot.exception.PlaceNotFoundException;
import io.project.townguidebot.mapper.PlaceMapper;
import io.project.townguidebot.model.Place;
import io.project.townguidebot.dto.PlaceDto;
import io.project.townguidebot.repository.PlaceRepository;
import io.project.townguidebot.service.CityService;
import io.project.townguidebot.service.CloudinaryService;
import io.project.townguidebot.service.PhotoService;
import io.project.townguidebot.service.PlaceService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static io.project.townguidebot.service.constants.Prefixes.PLACE_PREFIX;

@Service
@Slf4j
@RequiredArgsConstructor
public class PlaceServiceImpl implements PlaceService {

    private final PlaceRepository placeRepository;
    private final PlaceMapper placeMapper;
    private final CityService cityService;
    private final CloudinaryService cloudinaryService;
    private final PhotoService photoService;

    private final Map<Long, Long> placeForChat = new ConcurrentHashMap<>();

    /**
     * Находит место по ID
     *
     * @param id идентификатор места
     * @return найденное место {@link PlaceDto}
     */
    @Transactional(readOnly = true)
    @Override
    public PlaceDto findPlaceById(Long id) {
        log.debug("Find place by id: {}", id);
        return placeMapper.toPlaceDto(placeRepository.findById(id).orElseThrow(() -> {
            log.warn("Place with id: {} not found", id);
            return new PlaceNotFoundException(String.format("Place with id: %s not found", id));
        }));
    }

    /**
     * Обновляет существующее место
     *
     * @param id идентификатор места
     * @param placeDto объект места {@link PlaceDto}
     * @return обновленное место {@link PlaceDto}
     */
    @Transactional
    @Override
    public PlaceDto updatePlace(Long id, PlaceDto placeDto) {
        log.debug("Updating place by id: {}", id);
        Place existing = placeRepository.findById(id).orElseThrow(() -> {
            log.warn("Place with id: {} not found", id);
            return new PlaceNotFoundException(String.format("Place with id: %s not found", id));
        });

        existing.setName(placeDto.getName());
        existing.setDescription(placeDto.getDescription());
        existing.setCity(cityService.findCityById(placeDto.getCityId()));

        return placeMapper.toPlaceDto(placeRepository.save(existing));
    }

    /**
     * Удаляет место по ID
     *
     * @param id идентификатор места
     */
    @Transactional
    @Override
    public void deletePlace(Long id) {
        log.debug("Delete place with id: {}", id);
        if (placeRepository.existsById(id)) {
            placeRepository.deleteById(id);
        } else {
            log.warn("Place with id: {} not found", id);
            throw new PlaceNotFoundException(String.format("Place with id: %s not found", id));
        }
    }

    /**
     * Находит место в БД по рандомному ID
     *
     * @return История {@link Place}
     */
    @Transactional(readOnly = true)
    @Override
    public PlaceDto getRandomPlaceByCity(String cityName) {
        log.debug("Get random place for city: {}", cityName);
        return placeMapper.toPlaceDto(placeRepository.findRandomPlace(cityName).orElseThrow(() -> {
                    log.warn("Random place not found for city: {}", cityName);
                    return new PlaceNotFoundException("Random place not found");
                }));
    }

    @Override
    public Long getSelectedPlaceForChat(Long chatId) {
        log.debug("Get selected place for chat: {}", chatId);
        return placeForChat.get(chatId);
    }

    @Override
    public void unselectedPlaceForChat(Long chatId) {
        placeForChat.remove(chatId);
    }


    @Override
    public Long selectedPlace(String callbackData, Long chatId) {
        Long placeId;
        if (callbackData.startsWith(PLACE_PREFIX)) {
            placeId = Long.parseLong(callbackData.substring(PLACE_PREFIX.length()));
        } else {
            return placeForChat.get(chatId);
        }
        log.debug("Select place: {} for chat: {}", placeId, chatId);
        Long id = placeForChat.putIfAbsent(chatId, placeId);
        return id == null ? placeForChat.get(chatId) : id;
    }

    @Override
    public List<Place> getPlacesByNameCity(String cityName) {
        log.debug("Find places by city name: {}", cityName);
        return placeRepository.findPlacesByCityName(cityName).orElseThrow(() -> {
            log.warn("Places by city: {} not found", cityName);
            return new PlaceNotFoundException(String.format("Places by city: %s not found", cityName));
        });
    }

    /**
     * Создает новое место с фотографией
     *
     * @param req {@link PlaceCreateRq}
     * @param file {@link MultipartFile}
     * @return {@link PlaceCreateRs}
     * @throws IOException Ошибка при обработке файла
     */
    @Override
    public PlaceCreateRs create(PlaceCreateRq req, UploadedFile file) throws IOException {
        log.debug("Create new place with photo");
        Place place = placeMapper.to(req);
        place.setCity(cityService.findCityById(req.getCityId()));
        placeRepository.save(place);
        UploadPhotoResult uploadResult = cloudinaryService.uploadPhoto(file, place.getId());
        photoService.savePhoto(place.getId(), uploadResult);
        return placeMapper.toRs(place);
    }
}

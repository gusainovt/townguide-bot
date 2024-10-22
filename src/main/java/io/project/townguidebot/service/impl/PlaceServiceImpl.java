package io.project.townguidebot.service.impl;

import io.project.townguidebot.exception.PlaceNotFoundException;
import io.project.townguidebot.mapper.PlaceMapper;
import io.project.townguidebot.model.Place;
import io.project.townguidebot.dto.PlaceDto;
import io.project.townguidebot.repository.PlaceRepository;
import io.project.townguidebot.service.PlaceService;
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
    private final Map<Long, Long> placeForChat = new ConcurrentHashMap<>();

    /**
     * Находит место по ID
     * @param id идентификатор места
     * @return найденное место {@link PlaceDto}
     */
    @Transactional(readOnly = true)
    @Override
    public PlaceDto findPlaceById(Long id) {
        log.info("Find place by id: {}", id);
        return placeMapper.toPlaceDto(placeRepository.findById(id).orElseThrow(() -> {
            log.error("Place with id: {} not found", id);
            return new PlaceNotFoundException(String.format("Place with id: %s not found", id));
        }));
    }

    /**
     * Создает новое место
     * @param placeDto объект места {@link PlaceDto}
     * @return созданное место {@link PlaceDto}
     */
    @Transactional
    @Override
    public PlaceDto createPlace(PlaceDto placeDto) {
        log.info("Create new palace...");
        Place place = placeMapper.toPlace(placeDto);
        return placeMapper.toPlaceDto(placeRepository.save(place));
    }

    /**
     * Обновляет существующее место
     * @param id идентификатор места
     * @param placeDto объект места {@link PlaceDto}
     * @return обновленное место {@link PlaceDto}
     */
    @Transactional
    @Override
    public PlaceDto updatePlace(Long id, PlaceDto placeDto) {
        log.info("Updating place by id: {}", id);
        if (placeRepository.existsById(id)) {
            Place place = placeMapper.toPlace(placeDto);
            place.setId(id);
            return placeMapper.toPlaceDto(placeRepository.save(place));
        } else {
            log.error("Place with id: {} not found", id);
            throw new PlaceNotFoundException(String.format("Place with id: %s not found", id));
        }
    }

    /**
     * Удаляет место по ID
     * @param id идентификатор места
     */
    @Transactional
    @Override
    public void deletePlace(Long id) {
        log.info("Delete place with id: {}", id);
        if (placeRepository.existsById(id)) {
            placeRepository.deleteById(id);
        } else {
            log.error("Place with id: {} not found", id);
            throw new PlaceNotFoundException(String.format("Place with id: %s not found", id));
        }
    }

    /**
     * Находит место в БД по рандомному ID
     * @return История {@link Place}
     */
    @Transactional(readOnly = true)
    @Override
    public PlaceDto getRandomPlaceByCity(String cityName) {
        log.info("Get random place for city: {}", cityName);
        return placeMapper.toPlaceDto(placeRepository.findRandomPlace(cityName).orElseThrow(() -> {
                    log.error("Random place not found");
                    return new PlaceNotFoundException("Random place not found");
                }));
    }

    @Override
    public Long getSelectedPlaceForChat(Long chatId) {
        log.info("Get selected city for chat: {}", chatId);
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
        log.info("Select city: {} for chat: {}", placeId, chatId);
        Long id = placeForChat.putIfAbsent(chatId, placeId);
        return id == null ? placeForChat.get(chatId) : id;
    }

    @Override
    public List<Place> getPlacesByNameCity(String cityName) {
        log.info("Find places by city name: {}", cityName);
        return placeRepository.findPlacesByCityName(cityName).orElseThrow(() -> {
            log.error("Places by city: {} not found", cityName);
            return new PlaceNotFoundException(String.format("Places by city: %s not found", cityName));
        });
    }

}

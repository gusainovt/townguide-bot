package io.project.townguidebot.service.impl;

import io.project.townguidebot.dto.PlaceDto;
import io.project.townguidebot.exception.PlaceNotFoundException;
import io.project.townguidebot.mapper.PlaceMapper;
import io.project.townguidebot.model.Place;
import io.project.townguidebot.repository.PlaceRepository;
import io.project.townguidebot.service.CityService;
import io.project.townguidebot.service.CloudinaryService;
import io.project.townguidebot.service.PhotoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static io.project.townguidebot.service.constants.Prefixes.PLACE_PREFIX;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlaceServiceImplTest {

    @Mock
    private PlaceRepository placeRepository;

    @Mock
    private PlaceMapper placeMapper;

    @Mock
    private CityService cityService;

    @Mock
    private CloudinaryService cloudinaryService;

    @Mock
    private PhotoService photoService;

    @InjectMocks
    private PlaceServiceImpl placeService;

    @Test
    void findPlaceById_WhenExists_ShouldReturnDto() {
        Long id = 1L;
        Place place = new Place();
        PlaceDto dto = new PlaceDto();
        when(placeRepository.findById(id)).thenReturn(Optional.of(place));
        when(placeMapper.toPlaceDto(place)).thenReturn(dto);

        PlaceDto result = placeService.findPlaceById(id);

        assertSame(dto, result);
        verify(placeRepository).findById(id);
        verify(placeMapper).toPlaceDto(place);
    }

    @Test
    void findPlaceById_WhenNotFound_ShouldThrowException() {
        Long id = 1L;
        when(placeRepository.findById(id)).thenReturn(Optional.empty());

        PlaceNotFoundException ex = assertThrows(PlaceNotFoundException.class,
                () -> placeService.findPlaceById(id));

        assertEquals("Place with id: 1 not found", ex.getMessage());
        verify(placeRepository).findById(id);
        verify(placeMapper, never()).toPlaceDto(any());
    }

    @Test
    void updatePlace_WhenExists_ShouldSaveWithIdAndReturnDto() {
        Long id = 7L;
        PlaceDto input = new PlaceDto();
        input.setCityId(0L);
        Place existing = new Place();
        Place saved = new Place();
        PlaceDto output = new PlaceDto();

        when(placeRepository.findById(id)).thenReturn(java.util.Optional.of(existing));
        when(cityService.findCityById(0L)).thenReturn(null);
        when(placeRepository.save(existing)).thenReturn(saved);
        when(placeMapper.toPlaceDto(saved)).thenReturn(output);

        PlaceDto result = placeService.updatePlace(id, input);

        assertSame(output, result);
        verify(placeRepository).findById(id);
        verify(placeRepository).save(existing);
        verify(placeMapper).toPlaceDto(saved);
    }

    @Test
    void updatePlace_WhenNotFound_ShouldThrowException() {
        Long id = 7L;
        when(placeRepository.findById(id)).thenReturn(java.util.Optional.empty());

        PlaceNotFoundException ex = assertThrows(PlaceNotFoundException.class,
                () -> placeService.updatePlace(id, new PlaceDto()));

        assertEquals("Place with id: 7 not found", ex.getMessage());
        verify(placeRepository).findById(id);
        verify(placeRepository, never()).save(any());
    }

    @Test
    void deletePlace_WhenExists_ShouldDelete() {
        Long id = 2L;
        when(placeRepository.existsById(id)).thenReturn(true);

        placeService.deletePlace(id);

        verify(placeRepository).existsById(id);
        verify(placeRepository).deleteById(id);
    }

    @Test
    void deletePlace_WhenNotFound_ShouldThrowException() {
        Long id = 2L;
        when(placeRepository.existsById(id)).thenReturn(false);

        PlaceNotFoundException ex = assertThrows(PlaceNotFoundException.class,
                () -> placeService.deletePlace(id));

        assertEquals("Place with id: 2 not found", ex.getMessage());
        verify(placeRepository).existsById(id);
        verify(placeRepository, never()).deleteById(any());
    }

    @Test
    void getRandomPlaceByCity_WhenFound_ShouldReturnDto() {
        String city = "moscow";
        Place place = new Place();
        PlaceDto dto = new PlaceDto();
        when(placeRepository.findRandomPlace(city)).thenReturn(Optional.of(place));
        when(placeMapper.toPlaceDto(place)).thenReturn(dto);

        PlaceDto result = placeService.getRandomPlaceByCity(city);

        assertSame(dto, result);
        verify(placeRepository).findRandomPlace(city);
        verify(placeMapper).toPlaceDto(place);
    }

    @Test
    void getRandomPlaceByCity_WhenNotFound_ShouldThrowException() {
        String city = "moscow";
        when(placeRepository.findRandomPlace(city)).thenReturn(Optional.empty());

        PlaceNotFoundException ex = assertThrows(PlaceNotFoundException.class,
                () -> placeService.getRandomPlaceByCity(city));

        assertEquals("Random place not found", ex.getMessage());
        verify(placeRepository).findRandomPlace(city);
    }

    @Test
    void selectedPlace_ShouldStoreAndReturnSelectedPlace() {
        Long chatId = 100L;
        Long placeId = 42L;

        Long result = placeService.selectedPlace(PLACE_PREFIX + placeId, chatId);

        assertEquals(placeId, result);
        assertEquals(placeId, placeService.getSelectedPlaceForChat(chatId));
    }

    @Test
    void selectedPlace_WhenAlreadySelected_ShouldReturnExistingPlace() {
        Long chatId = 100L;
        placeService.selectedPlace(PLACE_PREFIX + "42", chatId);

        Long result = placeService.selectedPlace(PLACE_PREFIX + "43", chatId);

        assertEquals(42L, result);
        assertEquals(42L, placeService.getSelectedPlaceForChat(chatId));
    }

    @Test
    void selectedPlace_WhenCallbackWithoutPrefix_ShouldReturnCurrentSelectedPlace() {
        Long chatId = 100L;
        placeService.selectedPlace(PLACE_PREFIX + "42", chatId);

        Long result = placeService.selectedPlace("CITY:moscow", chatId);

        assertEquals(42L, result);
    }

    @Test
    void selectedPlace_WhenCallbackWithoutPrefixAndNoSelection_ShouldReturnNull() {
        Long chatId = 100L;

        Long result = placeService.selectedPlace("CITY:moscow", chatId);

        assertNull(result);
    }

    @Test
    void unselectedPlaceForChat_ShouldRemoveSelection() {
        Long chatId = 100L;
        placeService.selectedPlace(PLACE_PREFIX + "42", chatId);

        placeService.unselectedPlaceForChat(chatId);

        assertNull(placeService.getSelectedPlaceForChat(chatId));
    }

    @Test
    void getPlacesByNameCity_WhenFound_ShouldReturnPlaces() {
        String city = "moscow";
        List<Place> places = List.of(new Place(), new Place());
        when(placeRepository.findPlacesByCityName(city)).thenReturn(Optional.of(places));

        List<Place> result = placeService.getPlacesByNameCity(city);

        assertSame(places, result);
        verify(placeRepository).findPlacesByCityName(city);
    }

    @Test
    void getPlacesByNameCity_WhenNotFound_ShouldThrowException() {
        String city = "moscow";
        when(placeRepository.findPlacesByCityName(city)).thenReturn(Optional.empty());

        PlaceNotFoundException ex = assertThrows(PlaceNotFoundException.class,
                () -> placeService.getPlacesByNameCity(city));

        assertEquals("Places by city: moscow not found", ex.getMessage());
        verify(placeRepository).findPlacesByCityName(city);
    }
}

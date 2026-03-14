package io.project.townguidebot.service.impl;

import io.project.townguidebot.dto.request.CityCreateRq;
import io.project.townguidebot.dto.response.CityResponse;
import io.project.townguidebot.exception.CityNotFoundException;
import io.project.townguidebot.mapper.CityMapper;
import io.project.townguidebot.model.City;
import io.project.townguidebot.repository.CityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static io.project.townguidebot.service.constants.Prefixes.CITY_PREFIX;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CityServiceImplTest {

    @Mock
    private CityRepository cityRepository;

    @Mock
    private CityMapper cityMapper;

    @InjectMocks
    private CityServiceImpl cityService;

    private City city;
    private static final Long CHAT_ID = 100L;
    private static final Long CITY_ID = 1L;

    @BeforeEach
    void setUp() {
        city = new City();
        city.setId(CITY_ID);
        city.setName("Москва");
        city.setNameEng("moscow");
        city.setDescription("Capital city");
    }

    @Test
    void getAllCity_ShouldReturnAllCities() {
        List<City> cities = List.of(city);
        List<CityResponse> responses = List.of(CityResponse.builder().id(CITY_ID).name(city.getName()).build());
        when(cityRepository.findAll()).thenReturn(cities);
        when(cityMapper.toListCityResponse(cities)).thenReturn(responses);

        List<CityResponse> result = cityService.getAllCity();

        assertNotNull(result);
        assertEquals(responses, result);
        verify(cityRepository).findAll();
        verify(cityMapper).toListCityResponse(cities);
    }

    @Test
    void getSelectedCityForChat_WhenCitySelected_ShouldReturnCityName() {
        cityService.selectedCity(CITY_PREFIX + city.getNameEng(), CHAT_ID);

        String result = cityService.getSelectedCityForChat(CHAT_ID);

        assertEquals(city.getNameEng(), result);
    }

    @Test
    void getSelectedCityForChat_WhenCityNotSelected_ShouldReturnNull() {
        String result = cityService.getSelectedCityForChat(CHAT_ID);

        assertNull(result);
    }

    @Test
    void unselectedCityForChat_ShouldRemoveSelectedCity() {
        cityService.selectedCity(CITY_PREFIX + city.getNameEng(), CHAT_ID);

        cityService.unselectedCityForChat(CHAT_ID);

        assertNull(cityService.getSelectedCityForChat(CHAT_ID));
    }

    @Test
    void findCityById_WhenCityExists_ShouldReturnCity() {
        when(cityRepository.findById(CITY_ID)).thenReturn(Optional.of(city));

        City result = cityService.findCityById(CITY_ID);

        assertEquals(city, result);
        verify(cityRepository).findById(CITY_ID);
    }

    @Test
    void findCityById_WhenCityNotFound_ShouldThrowException() {
        when(cityRepository.findById(CITY_ID)).thenReturn(Optional.empty());

        CityNotFoundException exception = assertThrows(CityNotFoundException.class,
                () -> cityService.findCityById(CITY_ID));

        assertEquals("City with id: 1 not found", exception.getMessage());
        verify(cityRepository).findById(CITY_ID);
    }

    @Test
    void selectedCity_WhenPrefixAndFirstSelection_ShouldStoreAndReturnSelectedCity() {
        String result = cityService.selectedCity(CITY_PREFIX + city.getNameEng(), CHAT_ID);

        assertEquals(city.getNameEng(), result);
        assertEquals(city.getNameEng(), cityService.getSelectedCityForChat(CHAT_ID));
    }

    @Test
    void selectedCity_WhenPrefixAndCityAlreadySelected_ShouldReturnExistingCity() {
        cityService.selectedCity(CITY_PREFIX + "moscow", CHAT_ID);

        String result = cityService.selectedCity(CITY_PREFIX + "moscow", CHAT_ID);

        assertEquals("moscow", result);
        assertEquals("moscow", cityService.getSelectedCityForChat(CHAT_ID));
    }

    @Test
    void selectedCity_WhenCallbackWithoutPrefix_ShouldReturnCurrentSelectedCity() {
        cityService.selectedCity(CITY_PREFIX + "moscow", CHAT_ID);

        String result = cityService.selectedCity("PLACE:arsenalna", CHAT_ID);

        assertEquals("moscow", result);
        assertEquals("moscow", cityService.getSelectedCityForChat(CHAT_ID));
    }

    @Test
    void selectedCity_WhenCallbackWithoutPrefixAndNoSelection_ShouldReturnNull() {
        String result = cityService.selectedCity("PLACE:arsenalna", CHAT_ID);

        assertNull(result);
    }

    @Test
    void getDescriptionSelectedCity_WhenDescriptionExists_ShouldReturnDescription() {
        cityService.selectedCity(CITY_PREFIX + city.getNameEng(), CHAT_ID);
        when(cityRepository.findDescriptionByNameEng(city.getNameEng())).thenReturn(Optional.of(city.getDescription()));

        String result = cityService.getDescriptionSelectedCity(CHAT_ID);

        assertEquals(city.getDescription(), result);
        verify(cityRepository).findDescriptionByNameEng(city.getNameEng());
    }

    @Test
    void getDescriptionSelectedCity_WhenDescriptionNotFound_ShouldThrowException() {
        cityService.selectedCity(CITY_PREFIX + city.getNameEng(), CHAT_ID);
        when(cityRepository.findDescriptionByNameEng(city.getNameEng())).thenReturn(Optional.empty());

        CityNotFoundException exception = assertThrows(CityNotFoundException.class,
                () -> cityService.getDescriptionSelectedCity(CHAT_ID));

        assertEquals("City with name: moscow not found", exception.getMessage());
        verify(cityRepository).findDescriptionByNameEng(city.getNameEng());
    }

    @Test
    void getCityNameByNameEng_WhenCityExists_ShouldReturnCityName() {
        when(cityRepository.findCityNameByNameEng(city.getNameEng())).thenReturn(Optional.of(city.getName()));

        String result = cityService.getCityNameByNameEng(city.getNameEng());

        assertEquals(city.getName(), result);
        verify(cityRepository).findCityNameByNameEng(city.getNameEng());
    }

    @Test
    void getCityNameByNameEng_WhenCityNotFound_ShouldThrowException() {
        when(cityRepository.findCityNameByNameEng(city.getNameEng())).thenReturn(Optional.empty());

        CityNotFoundException exception = assertThrows(CityNotFoundException.class,
                () -> cityService.getCityNameByNameEng(city.getNameEng()));

        assertEquals("City with name: moscow not found", exception.getMessage());
        verify(cityRepository).findCityNameByNameEng(city.getNameEng());
    }

    @Test
    void create_ShouldBuildCallbackSaveAndReturnResponse() {
        CityCreateRq req = new CityCreateRq();
        req.setName("Moscow");
        req.setNameEng("moscow");
        req.setDescription("Capital city");

        City savedCity = new City();
        savedCity.setId(CITY_ID);
        savedCity.setName(req.getName());
        savedCity.setNameEng(req.getNameEng());
        savedCity.setDescription(req.getDescription());
        savedCity.setCallback("CITY");

        CityResponse response = CityResponse.builder()
                .id(CITY_ID)
                .name(req.getName())
                .nameEng(req.getNameEng())
                .description(req.getDescription())
                .callback("CITY")
                .build();

        when(cityRepository.save(any(City.class))).thenReturn(savedCity);
        when(cityMapper.toCityResponse(savedCity)).thenReturn(response);

        CityResponse result = cityService.create(req);

        assertSame(response, result);
        ArgumentCaptor<City> captor = ArgumentCaptor.forClass(City.class);
        verify(cityRepository).save(captor.capture());
        City cityToSave = captor.getValue();
        assertEquals("Moscow", cityToSave.getName());
        assertEquals("moscow", cityToSave.getNameEng());
        assertEquals("Capital city", cityToSave.getDescription());
        assertEquals("CITY", cityToSave.getCallback());
        verify(cityMapper).toCityResponse(savedCity);
    }
}

package io.project.townguidebot.service.impl;

import io.project.townguidebot.dto.response.CityResponse;
import io.project.townguidebot.model.Place;
import io.project.townguidebot.service.CityService;
import io.project.townguidebot.service.PlaceService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import static io.project.townguidebot.model.enums.ButtonCallback.CITY;
import static io.project.townguidebot.model.enums.ButtonCallback.PHOTO;
import static io.project.townguidebot.model.enums.ButtonCallback.PLACE;
import static io.project.townguidebot.model.enums.ButtonCallback.SELECT_CITY;
import static io.project.townguidebot.model.enums.ButtonCallback.SELECT_PLACE;
import static io.project.townguidebot.model.enums.ButtonCallback.STORY;
import static io.project.townguidebot.model.enums.ButtonCallback.WEATHER;
import static io.project.townguidebot.service.constants.NameButtons.PHOTO_BUTTON;
import static io.project.townguidebot.service.constants.NameButtons.PLACE_BUTTON;
import static io.project.townguidebot.service.constants.NameButtons.SELECT_CITY_BUTTON;
import static io.project.townguidebot.service.constants.NameButtons.STORY_BUTTON;
import static io.project.townguidebot.service.constants.NameButtons.WEATHER_BUTTON;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuServiceImplTest {

    @Mock
    private CityService cityService;

    @Mock
    private PlaceService placeService;

    @InjectMocks
    private MenuServiceImpl menuService;

    @Test
    void startMenu_ShouldBuildButtonsForAllCities() {
        CityResponse moscow = CityResponse.builder()
                .name("Moscow")
                .nameEng("moscow")
                .build();
        CityResponse spb = CityResponse.builder()
                .name("Saint Petersburg")
                .nameEng("saint_petersburg")
                .build();

        when(cityService.getAllCity()).thenReturn(List.of(moscow, spb));

        InlineKeyboardMarkup markup = menuService.startMenu();

        assertNotNull(markup);
        assertNotNull(markup.getKeyboard());
        assertEquals(2, markup.getKeyboard().size());

        InlineKeyboardButton first = markup.getKeyboard().get(0).get(0);
        assertEquals(moscow.getName(), first.getText());
        assertEquals(CITY.name() + ":" + moscow.getNameEng(), first.getCallbackData());

        InlineKeyboardButton second = markup.getKeyboard().get(1).get(0);
        assertEquals(spb.getName(), second.getText());
        assertEquals(CITY.name() + ":" + spb.getNameEng(), second.getCallbackData());

        verify(cityService).getAllCity();
    }

    @Test
    void placeMenu_ShouldSetReplyMarkupWithPlaces() {
        String cityName = "moscow";
        Place place1 = new Place();
        place1.setId(1L);
        place1.setName("Red Square");
        Place place2 = new Place();
        place2.setId(2L);
        place2.setName("VDNKh");

        when(placeService.getPlacesByNameCity(cityName)).thenReturn(List.of(place1, place2));
        SendMessage message = new SendMessage();

        SendMessage result = menuService.placeMenu(message, cityName);

        assertSame(message, result);
        assertNotNull(result.getReplyMarkup());
        InlineKeyboardMarkup markup = (InlineKeyboardMarkup) result.getReplyMarkup();
        assertEquals(2, markup.getKeyboard().size());

        InlineKeyboardButton first = markup.getKeyboard().get(0).get(0);
        assertEquals(place1.getName(), first.getText());
        assertEquals(PLACE.name() + ":" + place1.getId(), first.getCallbackData());

        InlineKeyboardButton second = markup.getKeyboard().get(1).get(0);
        assertEquals(place2.getName(), second.getText());
        assertEquals(PLACE.name() + ":" + place2.getId(), second.getCallbackData());

        verify(placeService).getPlacesByNameCity(cityName);
    }

    @Test
    void cityMenu_ShouldBuildCityMenuWithTwoRows() {
        InlineKeyboardMarkup markup = menuService.cityMenu();

        assertNotNull(markup);
        assertNotNull(markup.getKeyboard());
        assertEquals(2, markup.getKeyboard().size());
        assertEquals(2, markup.getKeyboard().get(0).size());
        assertEquals(2, markup.getKeyboard().get(1).size());

        InlineKeyboardButton storyButton = markup.getKeyboard().get(0).get(0);
        assertEquals(STORY_BUTTON, storyButton.getText());
        assertEquals(STORY.toString(), storyButton.getCallbackData());

        InlineKeyboardButton placeButton = markup.getKeyboard().get(0).get(1);
        assertEquals(PLACE_BUTTON, placeButton.getText());
        assertEquals(SELECT_PLACE.toString(), placeButton.getCallbackData());

        InlineKeyboardButton weatherButton = markup.getKeyboard().get(1).get(0);
        assertEquals(WEATHER_BUTTON, weatherButton.getText());
        assertEquals(WEATHER.toString(), weatherButton.getCallbackData());

        InlineKeyboardButton selectCityButton = markup.getKeyboard().get(1).get(1);
        assertEquals(SELECT_CITY_BUTTON, selectCityButton.getText());
        assertEquals(SELECT_CITY.toString(), selectCityButton.getCallbackData());
    }

    @Test
    void photoMenu_ShouldSetReplyMarkupWithPhotoButton() {
        SendMessage message = new SendMessage();

        SendMessage result = menuService.photoMenu(message);

        assertSame(message, result);
        assertNotNull(result.getReplyMarkup());
        InlineKeyboardMarkup markup = (InlineKeyboardMarkup) result.getReplyMarkup();
        assertEquals(1, markup.getKeyboard().size());
        assertEquals(1, markup.getKeyboard().get(0).size());

        InlineKeyboardButton photoButton = markup.getKeyboard().get(0).get(0);
        assertEquals(PHOTO_BUTTON, photoButton.getText());
        assertEquals(PHOTO.toString(), photoButton.getCallbackData());
    }
}

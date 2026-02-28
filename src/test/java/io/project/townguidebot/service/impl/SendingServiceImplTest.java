package io.project.townguidebot.service.impl;

import com.vdurmont.emoji.EmojiParser;
import io.project.townguidebot.client.WeatherClient;
import io.project.townguidebot.dto.PlaceDto;
import io.project.townguidebot.dto.StoryDto;
import io.project.townguidebot.dto.Weather;
import io.project.townguidebot.dto.WeatherMain;
import io.project.townguidebot.dto.WeatherWind;
import io.project.townguidebot.service.CityService;
import io.project.townguidebot.service.MenuService;
import io.project.townguidebot.service.PlaceService;
import io.project.townguidebot.service.StoryService;
import io.project.townguidebot.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.io.IOException;
import java.math.BigDecimal;

import static io.project.townguidebot.service.constants.TelegramText.CITY_UNSELECTED;
import static io.project.townguidebot.service.constants.TelegramText.HELP_TEXT;
import static io.project.townguidebot.service.constants.TelegramText.NOT_FOUND_COMMAND;
import static io.project.townguidebot.service.constants.TelegramText.PLACE_UNSELECTED;
import static io.project.townguidebot.service.constants.TelegramText.SELECT_CITY;
import static io.project.townguidebot.service.constants.TelegramText.SELECT_PLACE;
import static io.project.townguidebot.service.constants.TelegramText.TEXT_WEATHER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SendingServiceImplTest {

    @Mock
    private MenuService menuService;

    @Mock
    private WeatherClient weatherClient;

    @Mock
    private UserService userService;

    @Mock
    private StoryService storyService;

    @Mock
    private CityService cityService;

    @Mock
    private PlaceService placeService;

    private SendingServiceImpl createService() {
        return new SendingServiceImpl(menuService, weatherClient, userService, storyService, cityService, placeService);
    }

    @Test
    void sendEditMessageText_ShouldFillFields() {
        SendingServiceImpl service = createService();

        EditMessageText result = service.sendEditMessageText("hi", 10L, 20L);

        assertEquals("10", result.getChatId());
        assertEquals("hi", result.getText());
        assertEquals(20, result.getMessageId());
    }

    @Test
    void sendMessage_ShouldFillFields() {
        SendingServiceImpl service = createService();

        SendMessage result = service.sendMessage(10L, "text");

        assertEquals("10", result.getChatId());
        assertEquals("text", result.getText());
    }

    @Test
    void commandNotFound_ShouldReturnParsedText() {
        SendingServiceImpl service = createService();

        SendMessage result = service.commandNotFound(10L);

        assertEquals(EmojiParser.parseToUnicode(NOT_FOUND_COMMAND), result.getText());
    }

    @Test
    void cityNotSelected_ShouldReturnParsedText() {
        SendingServiceImpl service = createService();

        SendMessage result = service.cityNotSelected(10L);

        assertEquals(EmojiParser.parseToUnicode(CITY_UNSELECTED), result.getText());
    }

    @Test
    void placeNotSelected_ShouldReturnParsedText() {
        SendingServiceImpl service = createService();

        SendMessage result = service.placeNotSelected(10L);

        assertEquals(EmojiParser.parseToUnicode(PLACE_UNSELECTED), result.getText());
    }

    @Test
    void sendStartPhoto_ShouldFillChatCaptionAndPhoto() throws IOException {
        SendingServiceImpl service = createService();

        SendPhoto result = service.sendStartPhoto(10L, "cap");

        assertEquals("10", result.getChatId());
        assertEquals("cap", result.getCaption());
        assertNotNull(result.getPhoto());
    }

    @Test
    void startCommandReceived_ShouldUnselectCityAndReturnPhotoWithStartMenu() throws IOException {
        SendingServiceImpl raw = createService();
        SendingServiceImpl service = Mockito.spy(raw);
        InlineKeyboardMarkup startMenu = new InlineKeyboardMarkup();
        SendPhoto stubPhoto = new SendPhoto();
        stubPhoto.setChatId(10L);

        when(userService.getNameByChatId(10L)).thenReturn("Alex");
        when(menuService.startMenu()).thenReturn(startMenu);
        doReturn(stubPhoto).when(service).sendStartPhoto(eq(10L), any(String.class));

        SendPhoto result = service.startCommandReceived(10L);

        assertSame(stubPhoto, result);
        assertSame(startMenu, result.getReplyMarkup());
        verify(cityService).unselectedCityForChat(10L);
        verify(userService).getNameByChatId(10L);
        verify(menuService).startMenu();
    }

    @Test
    void sendWeather_WhenCityNotSelected_ShouldReturnCityNotSelected() {
        SendingServiceImpl service = createService();
        when(cityService.getSelectedCityForChat(10L)).thenReturn(null);

        SendMessage result = service.sendWeather(10L);

        assertEquals(EmojiParser.parseToUnicode(CITY_UNSELECTED), result.getText());
        verify(weatherClient, never()).getWeather(any());
    }

    @Test
    void sendWeather_WhenCitySelected_ShouldReturnFormattedWeatherText() {
        SendingServiceImpl service = createService();
        when(cityService.getSelectedCityForChat(10L)).thenReturn("moscow");
        when(cityService.getCityNameByNameEng("moscow")).thenReturn("Москва");

        WeatherMain main = new WeatherMain();
        main.setTemp(BigDecimal.valueOf(11.2));
        main.setFeelsLike(BigDecimal.valueOf(9.7));
        WeatherWind wind = new WeatherWind();
        wind.setSpeed(BigDecimal.valueOf(3.4));
        Weather weather = new Weather();
        weather.setMain(main);
        weather.setWind(wind);
        when(weatherClient.getWeather("moscow")).thenReturn(weather);

        SendMessage result = service.sendWeather(10L);

        assertEquals(
                String.format(TEXT_WEATHER, "Москва", main.getTemp().toBigInteger(), main.getFeelsLike().toBigInteger(), wind.getSpeed().toString()),
                result.getText()
        );
        verify(weatherClient).getWeather("moscow");
    }

    @Test
    void sendPhoto_ShouldFillChatAndPhoto() throws IOException {
        SendingServiceImpl service = createService();

        SendPhoto result = service.sendPhoto(10L, "http://example.com/a.png");

        assertEquals("10", result.getChatId());
        assertNotNull(result.getPhoto());
    }

    @Test
    void sendRandomStory_WhenCityNotSelected_ShouldReturnCityNotSelected() {
        SendingServiceImpl service = createService();
        when(cityService.getSelectedCityForChat(10L)).thenReturn(null);

        SendMessage result = service.sendRandomStory(10L);

        assertEquals(EmojiParser.parseToUnicode(CITY_UNSELECTED), result.getText());
        verify(storyService, never()).getRandomStoryForCity(any());
    }

    @Test
    void sendRandomStory_WhenCitySelected_ShouldReturnStoryWithCityMenu() {
        SendingServiceImpl service = createService();
        when(cityService.getSelectedCityForChat(10L)).thenReturn("moscow");
        StoryDto story = new StoryDto();
        story.setBody("story text");
        when(storyService.getRandomStoryForCity("moscow")).thenReturn(story);
        InlineKeyboardMarkup cityMenu = new InlineKeyboardMarkup();
        when(menuService.cityMenu()).thenReturn(cityMenu);

        SendMessage result = service.sendRandomStory(10L);

        assertEquals("10", result.getChatId());
        assertEquals("story text", result.getText());
        assertSame(cityMenu, result.getReplyMarkup());
        verify(storyService).getRandomStoryForCity("moscow");
        verify(menuService).cityMenu();
    }

    @Test
    void sendHelpText_ShouldReturnHelpText() {
        SendingServiceImpl service = createService();

        SendMessage result = service.sendHelpText(10L);

        assertEquals(HELP_TEXT, result.getText());
    }

    @Test
    void cityMenuReceived_ShouldReturnDescriptionWithCityMenuMarkup() {
        SendingServiceImpl service = createService();
        InlineKeyboardMarkup cityMenu = new InlineKeyboardMarkup();
        when(cityService.getDescriptionSelectedCity(10L)).thenReturn("desc");
        when(menuService.cityMenu()).thenReturn(cityMenu);

        SendMessage result = service.cityMenuReceived(10L);

        assertEquals("10", result.getChatId());
        assertEquals("desc", result.getText());
        assertSame(cityMenu, result.getReplyMarkup());
        verify(cityService).getDescriptionSelectedCity(10L);
        verify(menuService).cityMenu();
    }

    @Test
    void selectCityCommandReceived_ShouldUnselectCityAndReturnStartMenu() {
        SendingServiceImpl service = createService();
        InlineKeyboardMarkup startMenu = new InlineKeyboardMarkup();
        when(menuService.startMenu()).thenReturn(startMenu);

        SendMessage result = service.selectCityCommandReceived(10L);

        assertEquals(SELECT_CITY, result.getText());
        assertSame(startMenu, result.getReplyMarkup());
        verify(cityService).unselectedCityForChat(10L);
        verify(menuService).startMenu();
    }

    @Test
    void sendRandomPlace_WhenCityNotSelected_ShouldReturnCityNotSelected() {
        SendingServiceImpl service = createService();
        when(cityService.getSelectedCityForChat(10L)).thenReturn(null);

        SendMessage result = service.sendRandomPlace(10L);

        assertEquals(EmojiParser.parseToUnicode(CITY_UNSELECTED), result.getText());
        verify(placeService, never()).getRandomPlaceByCity(any());
    }

    @Test
    void sendRandomPlace_WhenCitySelected_ShouldUsePhotoMenu() {
        SendingServiceImpl service = createService();
        when(cityService.getSelectedCityForChat(10L)).thenReturn("moscow");
        PlaceDto dto = new PlaceDto();
        dto.setName("Place");
        dto.setDescription("Desc");
        when(placeService.getRandomPlaceByCity("moscow")).thenReturn(dto);
        when(menuService.photoMenu(any(SendMessage.class))).thenAnswer(inv -> inv.getArgument(0));

        SendMessage result = service.sendRandomPlace(10L);

        assertEquals("Place\nDesc", result.getText());
        verify(placeService).getRandomPlaceByCity("moscow");
        verify(menuService).photoMenu(any(SendMessage.class));
    }

    @Test
    void sendMenuPlaces_WhenCityNotSelected_ShouldReturnCityNotSelected() {
        SendingServiceImpl service = createService();
        when(cityService.getSelectedCityForChat(10L)).thenReturn(null);

        SendMessage result = service.sendMenuPlaces(10L);

        assertEquals(EmojiParser.parseToUnicode(CITY_UNSELECTED), result.getText());
        verify(menuService, never()).placeMenu(any(), any());
    }

    @Test
    void sendMenuPlaces_WhenCitySelected_ShouldDelegateToPlaceMenu() {
        SendingServiceImpl service = createService();
        when(cityService.getSelectedCityForChat(10L)).thenReturn("moscow");
        when(menuService.placeMenu(any(SendMessage.class), eq("moscow"))).thenAnswer(inv -> inv.getArgument(0));

        SendMessage result = service.sendMenuPlaces(10L);

        assertEquals(SELECT_PLACE, result.getText());
        verify(menuService).placeMenu(any(SendMessage.class), eq("moscow"));
    }

    @Test
    void sendSelectedPlace_WhenPlaceNotSelected_ShouldReturnPlaceNotSelected() {
        SendingServiceImpl service = createService();
        when(placeService.getSelectedPlaceForChat(10L)).thenReturn(null);

        SendMessage result = service.sendSelectedPlace(10L);

        assertEquals(EmojiParser.parseToUnicode(PLACE_UNSELECTED), result.getText());
        verify(placeService, never()).findPlaceById(any());
    }

    @Test
    void sendSelectedPlace_WhenSelected_ShouldUsePhotoMenuWithPlaceText() {
        SendingServiceImpl service = createService();
        when(placeService.getSelectedPlaceForChat(10L)).thenReturn(7L);
        PlaceDto dto = new PlaceDto();
        dto.setName("P");
        dto.setDescription("D");
        when(placeService.findPlaceById(7L)).thenReturn(dto);
        when(menuService.photoMenu(any(SendMessage.class))).thenAnswer(inv -> inv.getArgument(0));

        SendMessage result = service.sendSelectedPlace(10L);

        assertEquals("P\nD", result.getText());
        verify(placeService).findPlaceById(7L);
        verify(menuService).photoMenu(any(SendMessage.class));
    }

    @Test
    void sendMenuPlaces_ShouldPassMessageWithChatId() {
        SendingServiceImpl service = createService();
        when(cityService.getSelectedCityForChat(10L)).thenReturn("moscow");
        when(menuService.placeMenu(any(SendMessage.class), eq("moscow"))).thenAnswer(inv -> inv.getArgument(0));

        service.sendMenuPlaces(10L);

        ArgumentCaptor<SendMessage> captor = ArgumentCaptor.forClass(SendMessage.class);
        verify(menuService).placeMenu(captor.capture(), eq("moscow"));
        assertEquals("10", captor.getValue().getChatId());
    }
}


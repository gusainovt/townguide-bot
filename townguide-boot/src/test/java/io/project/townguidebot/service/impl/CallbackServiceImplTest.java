package io.project.townguidebot.service.impl;

import io.project.townguidebot.exception.EmptyMessageException;
import io.project.townguidebot.model.Photo;
import io.project.townguidebot.model.enums.ButtonCallback;
import io.project.townguidebot.service.PhotoService;
import io.project.townguidebot.service.PlaceService;
import io.project.townguidebot.service.SendingService;
import io.project.townguidebot.service.strategy.CallbackSendMessageStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CallbackServiceImplTest {

    @Mock
    private SendingService sendingService;

    @Mock
    private PhotoService photoService;

    @Mock
    private PlaceService placeService;

    @Mock
    private CallbackSendMessageStrategy cityStrategy;

    @Mock
    private CallbackSendMessageStrategy placeStrategy;

    private CallbackServiceImpl callbackService;
    private static final Long CHAT_ID = 101L;

    @BeforeEach
    void setUp() {
        when(cityStrategy.getButtonCallback()).thenReturn(ButtonCallback.CITY);
        when(placeStrategy.getButtonCallback()).thenReturn(ButtonCallback.PLACE);

        callbackService = new CallbackServiceImpl(
                sendingService,
                photoService,
                placeService,
                List.of(cityStrategy, placeStrategy)
        );
        callbackService.init();
    }

    @Test
    void init_ShouldRegisterStrategiesAndButtonStartShouldUseThem() {
        Update update = mockStartUpdate("PLACE", CHAT_ID);
        SendMessage expected = new SendMessage();
        when(placeStrategy.handle(any(Message.class))).thenReturn(expected);

        SendMessage result = callbackService.buttonStart(update);

        assertEquals(expected, result);
        verify(placeStrategy).handle(any(Message.class));
        verify(cityStrategy, never()).handle(any(Message.class));
    }

    @Test
    void buttonStart_WhenCallbackHasDataPart_ShouldResolveCallbackByPrefix() {
        Update update = mockStartUpdate("CITY:kyiv", CHAT_ID);
        SendMessage expected = new SendMessage();
        when(cityStrategy.handle(any(Message.class))).thenReturn(expected);

        SendMessage result = callbackService.buttonStart(update);

        assertEquals(expected, result);
        verify(cityStrategy).handle(any(Message.class));
    }

    @Test
    void buttonStart_WhenMessageIsMissing_ShouldThrowException() {
        Update update = mock(Update.class);
        when(update.getMessage()).thenReturn(null);
        when(update.hasCallbackQuery()).thenReturn(false);

        EmptyMessageException exception = assertThrows(
                EmptyMessageException.class,
                () -> callbackService.buttonStart(update)
        );

        assertEquals("Message is empty or not found", exception.getMessage());
    }

    @Test
    void buttonPlace_WhenPhotoCallbackAndSelectionExists_ShouldReturnPhoto() throws IOException {
        Long placeId = 10L;
        String url = "https://example.org/photo.jpg";
        Update update = mockPlaceUpdate("PHOTO", CHAT_ID);
        Photo photo = new Photo();
        photo.setUrl(url);
        SendPhoto expected = new SendPhoto();

        when(placeService.getSelectedPlaceForChat(CHAT_ID)).thenReturn(placeId);
        when(photoService.getAllPhotoByPlace(placeId)).thenReturn(List.of(photo));
        when(sendingService.sendPhoto(CHAT_ID, url)).thenReturn(expected);

        SendPhoto result = callbackService.buttonPlace(update);

        assertEquals(expected, result);
        verify(placeService).getSelectedPlaceForChat(CHAT_ID);
        verify(photoService).getAllPhotoByPlace(placeId);
        verify(sendingService).sendPhoto(CHAT_ID, url);
    }

    @Test
    void buttonPlace_WhenPhotoCallbackAndNoSelectedPlace_ShouldThrowException() {
        Update update = mockPlaceUpdate("PHOTO", CHAT_ID);
        when(placeService.getSelectedPlaceForChat(CHAT_ID)).thenReturn(null);

        assertThrows(NoSuchElementException.class, () -> callbackService.buttonPlace(update));
        verify(placeService).getSelectedPlaceForChat(CHAT_ID);
        verifyNoInteractions(photoService);
        verifyNoInteractions(sendingService);
    }

    @Test
    void buttonPlace_WhenSendingPhotoThrowsIOException_ShouldWrapInRuntimeException() throws IOException {
        Long placeId = 10L;
        String url = "https://example.org/photo.jpg";
        Update update = mockPlaceUpdate("PHOTO", CHAT_ID);
        Photo photo = new Photo();
        photo.setUrl(url);

        when(placeService.getSelectedPlaceForChat(CHAT_ID)).thenReturn(placeId);
        when(photoService.getAllPhotoByPlace(placeId)).thenReturn(List.of(photo));
        when(sendingService.sendPhoto(CHAT_ID, url)).thenThrow(new IOException("I/O failure"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> callbackService.buttonPlace(update));

        assertNotNull(exception.getCause());
        assertInstanceOf(IOException.class, exception.getCause());
    }

    @Test
    void buttonPlace_WhenNotPhotoCallback_ShouldReturnEmptySendPhoto() {
        Update update = mockPlaceUpdate("PLACE", CHAT_ID);

        SendPhoto result = callbackService.buttonPlace(update);

        assertNotNull(result);
        verifyNoInteractions(placeService);
        verifyNoInteractions(photoService);
        verifyNoInteractions(sendingService);
    }

    private Update mockStartUpdate(String callbackData, Long chatId) {
        Update update = mock(Update.class);
        CallbackQuery callbackQuery = mock(CallbackQuery.class);
        Message message = mock(Message.class);

        when(update.getMessage()).thenReturn(message);
        when(update.getCallbackQuery()).thenReturn(callbackQuery);
        when(callbackQuery.getData()).thenReturn(callbackData);
        when(message.getChatId()).thenReturn(chatId);
        return update;
    }

    private Update mockPlaceUpdate(String callbackData, Long chatId) {
        Update update = mock(Update.class);
        CallbackQuery callbackQuery = mock(CallbackQuery.class);
        Message message = mock(Message.class);

        when(update.getCallbackQuery()).thenReturn(callbackQuery);
        when(callbackQuery.getData()).thenReturn(callbackData);
        when(callbackQuery.getMessage()).thenReturn(message);
        when(message.getChatId()).thenReturn(chatId);
        return update;
    }
}

package io.project.townguidebot.service.strategy.impl;

import io.project.townguidebot.model.enums.ButtonCallback;
import io.project.townguidebot.service.SendingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CityHandlerTest {

    @Mock
    private SendingService sendingService;

    @Test
    void getButtonCallback_ShouldReturnCity() {
        CityHandler handler = new CityHandler(sendingService);

        assertEquals(ButtonCallback.CITY, handler.getButtonCallback());
    }

    @Test
    void handle_ShouldCallCityMenuReceived() {
        CityHandler handler = new CityHandler(sendingService);
        Message message = org.mockito.Mockito.mock(Message.class);
        when(message.getChatId()).thenReturn(10L);
        SendMessage expected = new SendMessage();
        when(sendingService.cityMenuReceived(10L)).thenReturn(expected);

        SendMessage result = handler.handle(message);

        assertSame(expected, result);
        verify(sendingService).cityMenuReceived(10L);
    }
}


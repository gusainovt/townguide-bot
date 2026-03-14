package io.project.townguidebot.service.util;

import java.util.Optional;
import lombok.experimental.UtilityClass;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@UtilityClass
public class MessageExtractor {
  public static Optional<Message> extract(Update update) {
    if (update.getMessage() instanceof Message m) {
      return Optional.of(m);
    }
    if (update.hasCallbackQuery()
        && update.getCallbackQuery().getMessage() instanceof Message m) {
      return Optional.of(m);
    }
    return Optional.empty();
  }
}

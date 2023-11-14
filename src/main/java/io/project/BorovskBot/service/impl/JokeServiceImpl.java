package io.project.BorovskBot.service.impl;

import io.project.BorovskBot.exception.JokeNotFoundException;
import io.project.BorovskBot.model.Joke;
import io.project.BorovskBot.repository.JokeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Random;

import static io.project.BorovskBot.service.constants.ErrorText.ERROR_TEXT;
import static io.project.BorovskBot.service.constants.LogText.METHOD_CALLED;
import static io.project.BorovskBot.service.constants.TelegramText.MAX_JOKE_ID_MINUS_ONE;
import static io.project.BorovskBot.service.constants.ErrorText.ERROR_JOKE_NOT_FOUND;

@Service
@Slf4j
@RequiredArgsConstructor
public class JokeServiceImpl implements io.project.BorovskBot.service.JokeService {

    private final JokeRepository jokeRepository;

    /**
     * Находит шутку в БД по рандомному ID
     * @return Шутка {@link Joke}
     */
    @Override
    public Joke getRandomJoke() {
        log.info(METHOD_CALLED + Thread.currentThread().getStackTrace()[2].getMethodName());
        var r = new Random();
        var randomId = r.nextInt(MAX_JOKE_ID_MINUS_ONE) + 1;

        return jokeRepository.findById(randomId).orElseThrow(()->{
            JokeNotFoundException jokeEx = new JokeNotFoundException(String.format(ERROR_JOKE_NOT_FOUND, randomId));
            log.error(ERROR_TEXT + jokeEx.getMessage());
            return jokeEx;
        });
    }


}

package io.project.BorovskBot.exception;

public class JokeNotFoundException extends RuntimeException{

    public JokeNotFoundException(String message) {
        super(message);
    }
}

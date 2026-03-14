package io.project.townguidebot.exception;

public class StoryNotFoundException extends RuntimeException{
    public StoryNotFoundException(String message) {
        super(message);
    }
}

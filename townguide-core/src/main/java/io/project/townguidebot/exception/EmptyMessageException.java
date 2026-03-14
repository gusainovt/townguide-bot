package io.project.townguidebot.exception;

public class EmptyMessageException extends RuntimeException {

  public EmptyMessageException(String message) {
    super(message);
  }
}

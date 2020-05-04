package io.github.kkw.api.exceptions;

public class WrongPasswordException extends Exception {
    public WrongPasswordException(String message) {
        super(message);
    }
}

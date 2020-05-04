package io.github.kkw.api.exceptions;

public class ClientExistsException extends Exception {
    public ClientExistsException(final String message) {
        super(message);
    }
}

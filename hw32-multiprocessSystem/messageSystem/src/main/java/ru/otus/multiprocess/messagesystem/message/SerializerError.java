package ru.otus.multiprocess.messagesystem.message;

public class SerializerError extends RuntimeException {

    public SerializerError(String message, Throwable cause) {
        super(message, cause);
    }
}

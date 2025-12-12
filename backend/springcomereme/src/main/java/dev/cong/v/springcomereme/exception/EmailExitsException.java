package dev.cong.v.springcomereme.exception;

public class EmailExitsException extends  RuntimeException{

    public EmailExitsException(String message) {
        super(message);
    }

    public EmailExitsException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmailExitsException(Throwable cause) {
        super(cause);
    }
}

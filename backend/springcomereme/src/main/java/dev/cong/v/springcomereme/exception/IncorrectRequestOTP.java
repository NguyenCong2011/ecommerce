package dev.cong.v.springcomereme.exception;

public class IncorrectRequestOTP extends  RuntimeException{

    public IncorrectRequestOTP(String message) {
        super(message);
    }

    public IncorrectRequestOTP(String message, Throwable cause) {
        super(message, cause);
    }

    public IncorrectRequestOTP(Throwable cause) {
        super(cause);
    }
}

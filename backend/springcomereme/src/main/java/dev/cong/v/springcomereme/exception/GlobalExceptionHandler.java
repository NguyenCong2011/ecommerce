package dev.cong.v.springcomereme.exception;

import dev.cong.v.springcomereme.response.ExceptionResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {



    @ExceptionHandler(EmailExitsException.class)
    public ResponseEntity<?> emailExits(EmailExitsException ex){

        ExceptionResponse response = ExceptionResponse.builder()
                .status(HttpStatus.FORBIDDEN.value())
                .message(ex.getMessage())
                .timeStamp(System.currentTimeMillis())
                .build();

        return  new ResponseEntity<>(response,HttpStatus.FORBIDDEN);

    }

    @ExceptionHandler(EmailNotFoundException.class)
    public  ResponseEntity<?> emailNotFound(EmailNotFoundException ex){

        ExceptionResponse response = ExceptionResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(ex.getMessage())
                .timeStamp(System.currentTimeMillis())
                .build();


        return  new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IncorrectRequestOTP.class)
    public  ResponseEntity<?> inccorectOTP(IncorrectRequestOTP ex){

        ExceptionResponse response = ExceptionResponse.builder()
                .status(HttpStatus.FORBIDDEN.value())
                .message(ex.getMessage())
                .timeStamp(System.currentTimeMillis())
                .build();


        return  new ResponseEntity<>(response,HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public  ResponseEntity<?> entityNotFound(EntityNotFoundException ex){
        ExceptionResponse response = ExceptionResponse.builder()
                .status(HttpStatus.FORBIDDEN.value())
                .message(ex.getMessage())
                .timeStamp(System.currentTimeMillis())
                .build();


        return  new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(DuplicateException.class)
    public  ResponseEntity<?> duplicateEx(DuplicateException ex){
        ExceptionResponse response = ExceptionResponse.builder()
                .status(HttpStatus.FORBIDDEN.value())
                .message(ex.getMessage())
                .timeStamp(System.currentTimeMillis())
                .build();

        return  new ResponseEntity<>(response,HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ChangePasswordException.class)
    public  ResponseEntity<?> changePassword(ChangePasswordException ex){
        ExceptionResponse response = ExceptionResponse.builder()
                .status(HttpStatus.FORBIDDEN.value())
                .message(ex.getMessage())
                .timeStamp(System.currentTimeMillis())
                .build();

        return  new ResponseEntity<>(response,HttpStatus.FORBIDDEN);
    }




}

package com.timelyMailer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDetails> handleValidantionError(MethodArgumentNotValidException ex, WebRequest we){
        return new ResponseEntity<>(new ErrorDetails(ex.getMessage(), LocalDateTime.now(), we.getDescription(false)), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleNotFound(ResourceNotFoundException ex, WebRequest we){
        return new ResponseEntity<ErrorDetails>(new ErrorDetails(ex.getMessage(), LocalDateTime.now(),we.getDescription(false)),HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorDetails> handleApi(ApiException ex, WebRequest we){
        return new ResponseEntity<ErrorDetails>(new ErrorDetails(ex.getMessage(),LocalDateTime.now(),we.getDescription(false)),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailNotFound.class)
    public ResponseEntity<ErrorDetails> handleEmailNotFound(EmailNotFound ex, WebRequest we){
        return new ResponseEntity<ErrorDetails>(new ErrorDetails(ex.getMessage(), LocalDateTime.now(),we.getDescription(false)),HttpStatus.NOT_FOUND);
    }
}

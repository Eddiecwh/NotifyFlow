package com.eddiecwh.NotificationSystem.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(JobNotFoundException.class)
    public ResponseEntity<String> handleJobNotFoundException(RuntimeException e) {
        return new ResponseEntity<>("No existing jobs found with provided Job ID " + e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RequestNotFoundException.class)
    public ResponseEntity<String> handleRequestNotFoundException(RuntimeException e) {
        return new ResponseEntity<>("No existing requests found with provided request ID " + e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(RuntimeException e) {
        return new ResponseEntity<>("No existing user found with provided user ID " + e, HttpStatus.NOT_FOUND);
    }
}

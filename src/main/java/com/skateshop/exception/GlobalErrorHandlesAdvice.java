package com.skateshop.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalErrorHandlesAdvice {

    @ExceptionHandler
    public ResponseEntity<DefaltErrorMessage> handleNotFoundException (NotFoundException e) {
        var error = new DefaltErrorMessage(HttpStatus.NOT_FOUND.value(), e.getReason());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler
    public ResponseEntity<DefaltErrorMessage> handleEmailAlreadyExistException (EmailAlreadyExistsException e) {
        var error = new DefaltErrorMessage(HttpStatus.BAD_REQUEST.value(), e.getReason());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler
    public ResponseEntity<DefaltErrorMessage> handleCpfAlreadyExistException (CpfAlreadyExistsException e) {
        var error = new DefaltErrorMessage(HttpStatus.BAD_REQUEST.value(), e.getReason());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler
    public ResponseEntity<DefaltErrorMessage> handlePhoneAlreadyExistException (PhoneAlreadyExistsException e) {
        var error = new DefaltErrorMessage(HttpStatus.BAD_REQUEST.value(), e.getReason());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}


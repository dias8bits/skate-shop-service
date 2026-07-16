package com.skateshop.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ValidationFieldError>> handleMethodArgumentNotValidException (MethodArgumentNotValidException e) {

        List<ValidationFieldError> erros = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(erro -> new ValidationFieldError(erro.getField(), erro.getDefaultMessage()))
                .toList();

        return ResponseEntity.badRequest().body(erros);
    }

}


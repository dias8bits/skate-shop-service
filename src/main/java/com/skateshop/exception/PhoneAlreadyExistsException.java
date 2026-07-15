package com.skateshop.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class PhoneAlreadyExistsException extends ResponseStatusException {
    public PhoneAlreadyExistsException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}

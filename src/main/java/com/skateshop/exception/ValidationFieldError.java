package com.skateshop.exception;

public record ValidationFieldError(String field, String message) {
}

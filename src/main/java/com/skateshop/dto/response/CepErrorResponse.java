package com.skateshop.dto.response;

import java.util.List;

public record CepErrorResponse(String name, String message, String type, List<CepInnerErrorResponse> errors) {
}

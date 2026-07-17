package com.skateshop.dto.response;

import lombok.Builder;

@Builder
public record CepInnerErrorResponse(String name, String message) {
}

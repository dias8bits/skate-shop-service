package com.skateshop.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Builder
public class AddressPostRequest {

    private String number;

    private String complement;

    @NotBlank(message = "the field 'cep' is required")
    private String cep;

}

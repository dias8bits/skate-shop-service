package com.skateshop.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AddressPutRequest {

    @NotBlank(message = "the field 'name' is required")
    private String number;

    @NotBlank(message = "the field 'complement' is required")
    private String complement;

    @NotBlank(message = "the field 'cep' is required")
    private String cep;

}

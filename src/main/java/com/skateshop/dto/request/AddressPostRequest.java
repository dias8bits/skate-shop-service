package com.skateshop.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Setter
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddressPostRequest {

    private String number;

    private String complement;

    @NotBlank(message = "the field 'cep' is required")
    private String cep;

}

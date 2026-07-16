package com.skateshop.dto.request;

import com.skateshop.domain.user.User;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class AddressPostRequest {

    private String number;

    private String complement;

    @NotBlank(message = "the field 'cep' is required")
    private String cep;

}

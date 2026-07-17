package com.skateshop.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AddressPatchRequest {

    private String number;

    private String complement;

    private String cep;

}

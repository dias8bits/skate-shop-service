package com.skateshop.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.skateshop.domain.user.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
@ToString
@JsonPropertyOrder({"id", "userID", "cep", "street", "number", "complement", "neighborhood", "city", "state"})
public class AddressGetResponse {

    private UUID id;

    private String street;

    private String number;

    private String complement;

    private String neighborhood;

    private String city;

    private String state;

    private String cep;

    private UUID userId;

}

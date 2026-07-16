package com.skateshop.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonPropertyOrder({"id", "firstName", "lastName", "email", "phone", "cpf"})
public class UserPutResponse {

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    private String cpf;

}

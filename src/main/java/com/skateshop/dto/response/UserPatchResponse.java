package com.skateshop.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserPatchResponse {

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    private String cpf;
}

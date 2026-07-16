package com.skateshop.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserPatchRequest {

    private String firstName;

    private String lastName;

    @Email(message = "invalid email")
    private String email;

    @Pattern(
            regexp = "^\\(?\\d{2}\\)?[\\s-]?\\d{4,5}-?\\d{4}$",
            message = "invalid phone")
    private String phone;

    @Pattern(
            regexp = "^\\d{3}\\.?\\d{3}\\.?\\d{3}-?\\d{2}$",
            message = "invalid cpf")
    private String cpf;
}

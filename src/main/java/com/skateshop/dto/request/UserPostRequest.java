package com.skateshop.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserPostRequest {

    @NotBlank(message = "the field 'first name' is required")
    private String firstName;

    @NotBlank(message = "the field 'last name' is required")
    private String lastName;

    @Email(message = "invalid email")
    @NotBlank(message = "the field 'email' is required")
    private String email;

    @Pattern(
            regexp = "^\\(?\\d{2}\\)?[\\s-]?\\d{4,5}-?\\d{4}$",
            message = "invalid phone")
    @NotBlank(message = "the field 'phone' is required")
    private String phone;

    @Pattern(
            regexp = "^\\d{3}\\.?\\d{3}\\.?\\d{3}-?\\d{2}$",
            message = "invalid cpf")
    @NotBlank(message = "the field 'cpf' is required")
    private String cpf;
}

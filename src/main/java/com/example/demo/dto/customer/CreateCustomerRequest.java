package com.example.demo.dto.customer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCustomerRequest {

    @NotBlank(message = "firstName is required")
    @Size(max = 50, message = "firstName must not exceed 50 characters")
    @Pattern(
            regexp = "^[\\u0600-\\u06FF0-9.\\- ]+$",
            message = "Only Persian letters, dots, dashes, and English digits are allowed."
    )
    private String firstName;

    @NotBlank(message = "lastName is required")
    @Size(max = 50, message = "lastName must not exceed 50 characters")
    @Pattern(
            regexp = "^[\\u0600-\\u06FF0-9.\\- ]+$",
            message = "Only Persian letters, dots, dashes, and English digits are allowed."
    )
    private String lastName;

    @NotBlank(message = "mobile is required")
    @Pattern(
            regexp = "^09\\d{9}$",
            message = "mobile must be 11 digits and start with 09"
    )
    private String mobile;

    @NotBlank(message = "nationalCode is required")
    @Pattern(regexp = "\\d{10}", message = "nationalCode must be exactly 10 digits")
    private String nationalCode;

}
package com.example.demo.dto.customer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateCustomerRequest {
    @NotBlank(message = "{validation.first-name.required}")
    @Size(max = 50, message = "{validation.first-name.max-length}")
    @Pattern(
            regexp = "^[\\u0600-\\u06FF0-9.\\- ]+$",
            message = "{validation.first-name.invalid}"
    )
    private String firstName;

    @NotBlank(message = "{validation.last-name.required}")
    @Size(max = 50, message = "{validation.last-name.max-length}")
    @Pattern(
            regexp = "^[\\u0600-\\u06FF0-9.\\- ]+$",
            message = "{validation.last-name.invalid}"
    )
    private String lastName;

    @NotBlank(message = "{validation.mobile.required}")
    @Pattern(
            regexp = "^09\\d{9}$",
            message = "{validation.mobile.invalid}"
    )
    private String mobile;

    @NotBlank(message = "{validation.national-code.required}")
    @Pattern(regexp = "\\d{10}", message = "{validation.national-code.invalid}")
    private String nationalCode;
}
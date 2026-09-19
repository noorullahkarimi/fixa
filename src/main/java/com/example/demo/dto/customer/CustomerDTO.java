package com.example.demo.dto.customer;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.UUID;

public class CustomerDTO {

    private UUID uuid;

    @Pattern(
            regexp = "^[\\u0600-\\u06FF0-9.\\- ]+$",
            message = "Only Persian letters, dots, dashes, and English digits are allowed."
    )
    @NotBlank(message = "name is mandatory")
    private String firstName;

    @Pattern(
            regexp = "^[\\u0600-\\u06FF0-9.\\- ]+$",
            message = "Only Persian letters, dots, dashes, and English digits are allowed."
    )
    @NotBlank(message = "letter only")
    private String lastName;

    @NotBlank(message = "Phone number is mandatory")
    @Pattern(
            regexp = "^09\\d{9}$",
            message = "mobile must be 11 digits and start with 09"
    )
    private String phoneNumber;

    @Pattern(regexp = "\\d{10}", message = "nationalCode must be exactly 10 digits")
    @NotBlank(message = "National ID number is mandatory")
    private String nationalID;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getNationalID() {
        return nationalID;
    }

    public void setNationalID(String nationalID) {
        this.nationalID = nationalID;
    }
}

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
            regexp = "^[\\u0600-\\u06FF\\s]+$",
            message = "letter only"
    )
    @NotBlank(message = "name is mandatory")
    private String firstName;

    @Pattern(
            regexp = "^[\\u0600-\\u06FF\\s]+$",
            message = "نام باید فقط شامل حروف فارسی و فاصله باشد"
    )
    @NotBlank(message = "letter only")
    private String lastName;

    @NotBlank(message = "Phone number is mandatory")
    @Pattern(
            regexp = "^(?:\\+98|0098)?09[0-9]{9}$",
            message = "Invalid Iranian mobile number"
    )
    private String phoneNumber;

    @Pattern(regexp = "[0-9]{10}", message = "National ID must be exactly 10 digits")
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

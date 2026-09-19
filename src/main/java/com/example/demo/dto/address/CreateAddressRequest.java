package com.example.demo.dto.address;


import jakarta.validation.constraints.*;
import java.util.UUID;

public class CreateAddressRequest {

    @NotBlank(message = "details is required")
    @Size(max = 500, message = "details must not exceed 500 characters")
    @Pattern(
            regexp = "^[\\u0600-\\u06FF0-9.\\- ]+$",
            message = "Only Persian letters, dots, dashes, spaces, and English digits are allowed."
    )
    private String details;

    @NotNull(message = "customerUuid is required")
    private UUID customerUuid;

    @NotNull(message = "regionUuid is required")
    private UUID regionUuid;

    @NotNull(message = "latitude is required")
    @DecimalMin(value = "0", message = "latitude must be >= 0")
    @DecimalMax(value = "90.0", message = "latitude must be <= 90")
    private Double latitude;

    @NotNull(message = "longitude is required")
    @DecimalMin(value = "0", message = "longitude must be >= 0")
    @DecimalMax(value = "180.0", message = "longitude must be <= 180")
    private Double longitude;

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public UUID getCustomerUuid() {
        return customerUuid;
    }

    public void setCustomerUuid(UUID customerUuid) {
        this.customerUuid = customerUuid;
    }

    public UUID getRegionUuid() {
        return regionUuid;
    }

    public void setRegionUuid(UUID regionUuid) {
        this.regionUuid = regionUuid;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
package com.example.demo.dto.address;

import jakarta.validation.constraints.*;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateAddressRequest {

    @NotBlank(message = "{validation.details.required}")
    @Size(max = 500, message = "{validation.details.max-length}")
    @Pattern(
            regexp = "^[\\u0600-\\u06FF0-9.\\- ]+$",
            message = "{validation.details.invalid}"
    )
    private String details;

    @NotNull(message = "{validation.region.required}")
    private UUID regionUuid;

    @NotNull(message = "{validation.latitude.required}")
    @DecimalMin(value = "0", message = "{validation.latitude.min}")
    @DecimalMax(value = "90.0", message = "{validation.latitude.required}")
    private Double latitude;

    @NotNull(message = "{validation.longitude.max}")
    @DecimalMin(value = "0", message = "{validation.longitude.min}")
    @DecimalMax(value = "180.0", message = "{validation.longitude.max}")
    private Double longitude;

}
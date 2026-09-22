package com.example.demo.dto.region;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;


@Getter
@Setter
public class CreateRegionRequest {

    @NotBlank(message = "{validation.name.required}")
    @Pattern(
            regexp = "^[\\u0600-\\u06FF0-9.\\- ]+$",
            message = "{validation.name.invalid}"
    )
    @Size(max = 100, message = "{validation.name.max-length}")
    private String name;

    private UUID parentUuid;

    private boolean enabled = true;
}

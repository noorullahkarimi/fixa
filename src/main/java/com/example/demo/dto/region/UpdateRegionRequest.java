package com.example.demo.dto.region;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public class UpdateRegionRequest {

    @NotBlank(message = "name is required")
    @Pattern(
            regexp = "^[\\u0600-\\u06FF0-9.\\- ]+$",
            message = "Only Persian letters, dots, dashes, and English digits are allowed."
    )
    @Size(max = 100, message = "name must not exceed 100 characters")
    private String name;

    private UUID parentUuid;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getParentUuid() {
        return parentUuid;
    }

    public void setParentUuid(UUID parentUuid) {
        this.parentUuid = parentUuid;
    }
}


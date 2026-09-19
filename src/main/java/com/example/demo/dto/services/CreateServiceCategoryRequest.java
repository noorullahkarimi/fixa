package com.example.demo.dto.services;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.UUID;


public class CreateServiceCategoryRequest {

    @NotBlank(message = "name is required")
    @Pattern(
            regexp = "^[\\u0600-\\u06FF0-9.\\- ]+$",
            message = "Only Persian letters, dots, dashes, and English digits are allowed."
    )
    @Size(max = 100, message = "name must not exceed 100 characters")
    private String name;

    private boolean enabled = true;

    private UUID parentId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public UUID getParentId() {
        return parentId;
    }

    public void setParentId(UUID parentId) {
        this.parentId = parentId;
    }
}

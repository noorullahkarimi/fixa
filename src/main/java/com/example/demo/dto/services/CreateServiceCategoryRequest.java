package com.example.demo.dto.services;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateServiceCategoryRequest {

    @NotBlank(message = "{validation.name.required}")
    @Pattern(
            regexp = "^[\\u0600-\\u06FF0-9.\\- ]+$",
            message = "{validation.name.invalid}"
    )
    @Size(max = 100, message = "{validation.name.max-length}")
    private String name;

    private boolean enabled = true;

    private UUID parentId;
}

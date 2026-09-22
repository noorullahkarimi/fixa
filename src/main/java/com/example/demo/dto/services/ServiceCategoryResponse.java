package com.example.demo.dto.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ServiceCategoryResponse {

    private UUID uuid;
    private UUID parentId;
    private String name;
    private boolean enabled;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<ServiceCategoryResponse> children = new ArrayList<>();
}

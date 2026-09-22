package com.example.demo.dto.region;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegionResponse {

    private UUID uuid;
    private String name;
    private boolean enabled;
    private UUID parentUuid;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}

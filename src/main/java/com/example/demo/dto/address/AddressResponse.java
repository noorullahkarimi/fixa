package com.example.demo.dto.address;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressResponse {

    private UUID uuid;
    private String details;

    private UUID customerUuid;
    private UUID regionUuid;

    private Double latitude;
    private Double longitude;

    private String regionName;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
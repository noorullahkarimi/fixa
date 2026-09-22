package com.example.demo.dto.customer;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerResponse {

    private Long id;
    private UUID uuid;
    private String firstName;
    private String lastName;
    private String mobile;
    private String nationalCode;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
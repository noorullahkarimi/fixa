package com.example.demo.dto.order;
import com.example.demo.enums.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
public class OrderResponse {

    private UUID uuid;
    private UUID customerUuid;
    private UUID addressUuid;
    private UUID serviceCategoryUuid;
    private String orderCode;
    private LocalDate requestedDate;
    private OrderStatus status;
    private String customerFullName;
    private String addressDetails;
    private String serviceCategoryName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
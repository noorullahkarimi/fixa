package com.example.demo.dto.order;
import com.example.demo.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class OrderStatusHistoryResponse {

    private Long id;
    private OrderStatus fromStatus;
    private OrderStatus toStatus;
    private LocalDateTime changedAt;
    private String comment;
    private UUID orderUuid;

}
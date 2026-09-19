package com.example.demo.dto.order;
import com.example.demo.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public class OrderStatusHistoryResponse {

    private Long id;
    private OrderStatus fromStatus;
    private OrderStatus toStatus;
    private LocalDateTime changedAt;
    private String comment;
    private UUID orderUuid;

    public UUID getOrderUuid() {
        return orderUuid;
    }

    public void setOrderUuid(UUID orderUuid) {
        this.orderUuid = orderUuid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrderStatus getFromStatus() {
        return fromStatus;
    }

    public void setFromStatus(OrderStatus fromStatus) {
        this.fromStatus = fromStatus;
    }

    public OrderStatus getToStatus() {
        return toStatus;
    }

    public void setToStatus(OrderStatus toStatus) {
        this.toStatus = toStatus;
    }

    public LocalDateTime getChangedAt() {
        return changedAt;
    }

    public void setChangedAt(LocalDateTime changedAt) {
        this.changedAt = changedAt;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
package com.example.demo.dto.order;

import com.example.demo.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;

public class ChangeOrderStatusRequest {

    @NotNull(message = "newStatus is required")
    private OrderStatus newStatus;

    private String comment;

    public OrderStatus getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(OrderStatus newStatus) {
        this.newStatus = newStatus;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}

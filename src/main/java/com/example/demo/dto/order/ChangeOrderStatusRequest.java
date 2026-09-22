package com.example.demo.dto.order;

import com.example.demo.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class ChangeOrderStatusRequest {

    @NotNull(message = "{validation.new-status.required}")
    private OrderStatus newStatus;

    @Pattern(
            regexp = "^[\\u0600-\\u06FF0-9.\\- ]+$",
            message = "{validation.details.invalid}"
    )
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

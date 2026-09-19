package com.example.demo.dto.order;

import java.util.UUID;


public class CreateOrderResponse {

    private String orderCode;

    private UUID uuid;

    public CreateOrderResponse() {
    }

    public CreateOrderResponse(UUID uuid, String orderCode) {
        this.uuid = uuid;
        this.orderCode = orderCode;
    }
    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }
}
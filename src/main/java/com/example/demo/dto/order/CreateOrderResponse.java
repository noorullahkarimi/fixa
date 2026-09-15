package com.example.demo.dto.order;

public class CreateOrderResponse {

    private Long id;
    private String orderCode;

    public CreateOrderResponse() {
    }

    public CreateOrderResponse(Long id, String orderCode) {
        this.id = id;
        this.orderCode = orderCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }
}
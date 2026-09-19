package com.example.demo.dto.order;

import com.example.demo.model.OrderStatusHistory;

public final class OrderStatusHistoryMapper {

    private OrderStatusHistoryMapper() {
    }

    public static OrderStatusHistoryResponse toResponse(OrderStatusHistory entity) {
        OrderStatusHistoryResponse response = new OrderStatusHistoryResponse();
        response.setId(entity.getId());
        response.setOrderUuid(entity.getOrder().getUuid());
        response.setFromStatus(entity.getFromStatus());
        response.setToStatus(entity.getToStatus());
        response.setChangedAt(entity.getChangedAt());
        response.setComment(entity.getComment());
        return response;
    }
}
package com.example.demo.dto.order;

import com.example.demo.model.Order;
import java.util.UUID;

public final class OrderMapper {

    private OrderMapper() {
    }

    public static CreateOrderResponse toCreateResponse(Order entity) {
        return new CreateOrderResponse(entity.getUuid(), entity.getOrderCode());
    }

    public static OrderResponse toResponse(Order entity) {
        OrderResponse response = new OrderResponse();
        response.setUuid(entity.getUuid());
        response.setOrderCode(entity.getOrderCode());
        response.setRequestedDate(entity.getRequestedDate());
        response.setStatus(entity.getStatus());
        response.setCustomerUuid(entity.getCustomer().getUuid());
        response.setCustomerFullName(
                entity.getCustomer().getFirstName() + " " + entity.getCustomer().getLastName());
        response.setAddressUuid(entity.getAddress().getUuid());
        response.setAddressDetails(entity.getAddress().getDetails());
        response.setServiceCategoryUuid(entity.getServiceCategory().getUuid());
        response.setServiceCategoryName(entity.getServiceCategory().getName());
        response.setCreatedAt(entity.getCreatedAt());
        response.setUpdatedAt(entity.getUpdatedAt());
        return response;
    }
}
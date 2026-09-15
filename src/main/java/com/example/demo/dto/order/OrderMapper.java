package com.example.demo.dto.order;

import com.example.demo.model.Order;

public final class OrderMapper {

    private OrderMapper() {
    }

    public static CreateOrderResponse toCreateResponse(Order entity) {
        return new CreateOrderResponse(entity.getId(), entity.getOrderCode());
    }

    public static OrderResponse toResponse(Order entity) {
        OrderResponse response = new OrderResponse();
        response.setId(entity.getId());
        response.setOrderCode(entity.getOrderCode());
        response.setRequestedDate(entity.getRequestedDate());
        response.setStatus(entity.getStatus());
        response.setCustomerId(entity.getCustomer().getId());
        response.setCustomerFullName(
                entity.getCustomer().getFirstName() + " " + entity.getCustomer().getLastName());
        response.setAddressId(entity.getAddress().getId());
        response.setAddressDetails(entity.getAddress().getDetails());
        response.setServiceCategoryId(entity.getServiceCategory().getId());
        response.setServiceCategoryName(entity.getServiceCategory().getName());
        response.setCreatedAt(entity.getCreatedAt());
        response.setUpdatedAt(entity.getUpdatedAt());
        return response;
    }
}
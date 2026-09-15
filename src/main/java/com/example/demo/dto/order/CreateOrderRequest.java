package com.example.demo.dto.order;


import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class CreateOrderRequest {

    @NotNull(message = "serviceCategoryId is required")
    private Long serviceCategoryId;

    @NotNull(message = "customerId is required")
    private Long customerId;

    @NotNull(message = "addressId is required")
    private Long addressId;

    @NotNull(message = "requestedDate is required")
    @FutureOrPresent(message = "requestedDate must be today or in the future")
    private LocalDate requestedDate;

    public Long getServiceCategoryId() {
        return serviceCategoryId;
    }

    public void setServiceCategoryId(Long serviceCategoryId) {
        this.serviceCategoryId = serviceCategoryId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public LocalDate getRequestedDate() {
        return requestedDate;
    }

    public void setRequestedDate(LocalDate requestedDate) {
        this.requestedDate = requestedDate;
    }
}

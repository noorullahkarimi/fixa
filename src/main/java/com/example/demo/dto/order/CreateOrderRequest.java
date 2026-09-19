package com.example.demo.dto.order;


import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;

public class CreateOrderRequest {

    @NotNull(message = "requestedDate is required")
    @FutureOrPresent(message = "requestedDate must be today or in the future")
    private LocalDate requestedDate;

    private UUID serviceCategoryUuid;

    @NotNull(message = "customerUuid is required")
    private UUID customerUuid;

    @NotNull(message = "addressUuid is required")
    private UUID addressUuid;

    public LocalDate getRequestedDate() {
        return requestedDate;
    }

    public void setRequestedDate(LocalDate requestedDate) {
        this.requestedDate = requestedDate;
    }

    public UUID getServiceCategoryUuid() {
        return serviceCategoryUuid;
    }

    public void setServiceCategoryUuid(UUID serviceCategoryUuid) {
        this.serviceCategoryUuid = serviceCategoryUuid;
    }

    public UUID getCustomerUuid() {
        return customerUuid;
    }

    public void setCustomerUuid(UUID customerUuid) {
        this.customerUuid = customerUuid;
    }

    public UUID getAddressUuid() {
        return addressUuid;
    }

    public void setAddressUuid(UUID addressUuid) {
        this.addressUuid = addressUuid;
    }
}

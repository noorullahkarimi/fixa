package com.example.demo.dto.order;
import com.example.demo.enums.OrderStatus;
import java.util.UUID;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class OrderResponse {


    private UUID uuid;
    private UUID customerUuid;
    private UUID addressUuid;
    private UUID serviceCategoryUuid;
    private String orderCode;
    private LocalDate requestedDate;
    private OrderStatus status;
    private String customerFullName;
    private String addressDetails;
    private String serviceCategoryName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
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
    public UUID getServiceCategoryUuid() {
        return serviceCategoryUuid;
    }

    public void setServiceCategoryUuid(UUID serviceCategoryUuid) {
        this.serviceCategoryUuid = serviceCategoryUuid;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public LocalDate getRequestedDate() {
        return requestedDate;
    }

    public void setRequestedDate(LocalDate requestedDate) {
        this.requestedDate = requestedDate;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public String getCustomerFullName() {
        return customerFullName;
    }

    public void setCustomerFullName(String customerFullName) {
        this.customerFullName = customerFullName;
    }

    public String getAddressDetails() {
        return addressDetails;
    }

    public void setAddressDetails(String addressDetails) {
        this.addressDetails = addressDetails;
    }

    public String getServiceCategoryName() {
        return serviceCategoryName;
    }

    public void setServiceCategoryName(String serviceCategoryName) {
        this.serviceCategoryName = serviceCategoryName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
package com.example.demo.dto.customer;


import com.example.demo.model.Customer;

public final class CustomerMapper {

    private CustomerMapper() {
    }

    public static Customer toEntity(CreateCustomerRequest request) {
        Customer entity = new Customer();
        entity.setFirstName(request.getFirstName());
        entity.setLastName(request.getLastName());
        entity.setMobile(request.getMobile());
        entity.setNationalCode(request.getNationalCode());
        return entity;
    }

    public static void updateEntity(Customer entity, UpdateCustomerRequest request) {
        entity.setFirstName(request.getFirstName());
        entity.setLastName(request.getLastName());
        entity.setMobile(request.getMobile());
        entity.setNationalCode(request.getNationalCode());
    }

    public static CustomerResponse toResponse(Customer entity) {
        CustomerResponse response = new CustomerResponse();
        response.setId(entity.getId());
        response.setUuid(entity.getUuid());
        response.setFirstName(entity.getFirstName());
        response.setLastName(entity.getLastName());
        response.setMobile(entity.getMobile());
        response.setNationalCode(entity.getNationalCode());
        response.setCreatedAt(entity.getCreatedAt());
        response.setUpdatedAt(entity.getUpdatedAt());
        return response;
    }
}

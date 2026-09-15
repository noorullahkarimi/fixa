package com.example.demo.dto.address;


import com.example.demo.model.Address;
import com.example.demo.model.Customer;
import com.example.demo.model.Region;

public final class AddressMapper {

    private AddressMapper() {
    }

    public static Address toEntity(CreateAddressRequest request, Customer customer, Region region) {
        Address entity = new Address();
        entity.setDetails(request.getDetails());
        entity.setCustomer(customer);
        entity.setLongitude(request.getLongitude());
        entity.setLatitude(request.getLatitude());
        entity.setRegion(region);
        return entity;
    }

    public static void updateEntity(Address entity, UpdateAddressRequest request, Region region) {
        entity.setDetails(request.getDetails());
        entity.setLatitude(request.getLatitude());
        entity.setLongitude(request.getLongitude());
        entity.setRegion(region);
    }

    public static AddressResponse toResponse(Address entity) {
        AddressResponse response = new AddressResponse();
        response.setId(entity.getId());
        response.setDetails(entity.getDetails());
        response.setCustomerId(entity.getCustomer().getId());
        response.setRegionId(entity.getRegion().getId());
        response.setRegionName(entity.getRegion().getName());
        response.setCreatedAt(entity.getCreatedAt());
        response.setUpdatedAt(entity.getUpdatedAt());
        response.setLatitude(entity.getLatitude());
        response.setLongitude(entity.getLongitude());
        return response;
    }
}
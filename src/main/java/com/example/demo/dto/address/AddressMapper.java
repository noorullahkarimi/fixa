package com.example.demo.dto.address;


import com.example.demo.model.Address;
import com.example.demo.model.Customer;
import com.example.demo.model.Region;
import java.util.UUID;

public final class AddressMapper {

    private AddressMapper() {
    }

    public static Address toEntity(
            CreateAddressRequest request,
            Customer customer,
            Region region
    ) {
        Address entity = new Address();

        entity.setDetails(request.getDetails());
        entity.setCustomer(customer);
        entity.setRegion(region);
        entity.setLatitude(request.getLatitude());
        entity.setLongitude(request.getLongitude());

        return entity;
    }

    public static void updateEntity(
            Address entity,
            UpdateAddressRequest request,
            Region region
    ) {
        entity.setDetails(request.getDetails());
        entity.setLatitude(request.getLatitude());
        entity.setLongitude(request.getLongitude());
        entity.setRegion(region);
    }

    public static AddressResponse toResponse(Address entity) {

        AddressResponse response = new AddressResponse();

        response.setUuid(entity.getUuid());
        response.setDetails(entity.getDetails());

        response.setCustomerUuid(
                entity.getCustomer().getUuid()
        );

        response.setRegionUuid(
                entity.getRegion().getUuid()
        );

        response.setRegionName(
                entity.getRegion().getName()
        );

        response.setCreatedAt(entity.getCreatedAt());
        response.setUpdatedAt(entity.getUpdatedAt());
        response.setLatitude(entity.getLatitude());
        response.setLongitude(entity.getLongitude());

        return response;
    }
}
package com.example.demo.service;

import com.example.demo.dto.address.AddressMapper;
import com.example.demo.dto.address.AddressResponse;
import com.example.demo.dto.address.CreateAddressRequest;
import com.example.demo.dto.address.UpdateAddressRequest;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Address;
import com.example.demo.model.Customer;
import com.example.demo.model.Region;
import com.example.demo.repository.AddressRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AddressService {

    private final AddressRepository addressRepository;
    private final CustomerService customerService;
    private final RegionService regionService;

    public AddressService(AddressRepository addressRepository,
                          CustomerService customerService,
                          RegionService regionService) {
        this.addressRepository = addressRepository;
        this.customerService = customerService;
        this.regionService = regionService;
    }

    @Transactional(readOnly = true)
    public List<AddressResponse> findAll() {
        return addressRepository.findAllActive()
                .stream()
                .map(AddressMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AddressResponse findById(Long id) {
        Address entity = getActiveEntity(id);
        return AddressMapper.toResponse(entity);
    }

    @Transactional(readOnly = true)
    public List<AddressResponse> findByCustomerUuid(UUID customerUuid) {
        customerService.getActiveEntity(customerUuid);

        return addressRepository.findByCustomerUuidAndNotDeleted(customerUuid)
                .stream()
                .map(AddressMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AddressResponse findByUuid(UUID uuid) {
        Address entity = getActiveEntity(uuid);
        return AddressMapper.toResponse(entity);
    }


    @Transactional
    public AddressResponse create(CreateAddressRequest request) {

        Customer customer =
                customerService.getActiveEntity(request.getCustomerUuid());

        Region region =
                regionService.getEnabledAndActive(request.getRegionUuid());

        Address entity =
                AddressMapper.toEntity(request, customer, region);

        Address saved = addressRepository.save(entity);

        return AddressMapper.toResponse(saved);
    }

    @Transactional
    public AddressResponse update(
            UUID uuid,
            UpdateAddressRequest request
    ) {
        Address entity = getActiveEntity(uuid);

        Region region =
                regionService.getEnabledAndActive(request.getRegionUuid());

        AddressMapper.updateEntity(entity, request, region);

        Address saved = addressRepository.save(entity);

        return AddressMapper.toResponse(saved);
    }

    // take address and customer id to check if it is belong together or not
    @Transactional(readOnly = true)
    public Address getActiveEntityBelongingToCustomer(
            UUID addressUuid,
            UUID customerUuid
    ) {
        return addressRepository
                .findByUuidAndCustomerUuidAndNotDeleted(
                        addressUuid,
                        customerUuid
                )
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Address not found or does not belong to the customer."
                ));
    }

    private Address getActiveEntity(UUID uuid) {
        return addressRepository.findByUuidAndNotDeleted(uuid)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Address not found. uuid=" + uuid));
    }

    private Address getActiveEntity(Long id) {
        return addressRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Address not found. id=" + id));
    }
}

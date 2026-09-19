package com.example.demo.controller;
import com.example.demo.dto.address.AddressResponse;
import com.example.demo.dto.address.CreateAddressRequest;
import com.example.demo.dto.address.UpdateAddressRequest;
import com.example.demo.service.AddressService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    private final AddressService service;

    public AddressController(AddressService service) {
        this.service = service;
    }


    // find all address is belong to this user
    @GetMapping("/by-customer/{customerUuid}")
    public ResponseEntity<List<AddressResponse>> findByCustomerUuid(
            @NotNull @PathVariable UUID customerUuid) {
        return ResponseEntity.ok(service.findByCustomerUuid(customerUuid));
    }

    @PostMapping
    public ResponseEntity<AddressResponse> create(
            @Valid @RequestBody CreateAddressRequest request) {
        AddressResponse response = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<AddressResponse> findByUuid(
            @NotNull @PathVariable UUID uuid) {
        return ResponseEntity.ok(service.findByUuid(uuid));
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<AddressResponse> update(
            @NotNull @PathVariable UUID uuid,
            @Valid @RequestBody UpdateAddressRequest request) {
        return ResponseEntity.ok(service.update(uuid, request));
    }
}
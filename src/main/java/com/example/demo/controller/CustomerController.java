package com.example.demo.controller;
import com.example.demo.dto.customer.CreateCustomerRequest;
import com.example.demo.dto.customer.CustomerResponse;
import com.example.demo.dto.customer.UpdateCustomerRequest;
import com.example.demo.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    private final CustomerService service;

    public CustomerController(CustomerService service) {
        this.service = service;
    }

    // find user by uuid
    @GetMapping("/{uuid}")
    public ResponseEntity<CustomerResponse> findById(@PathVariable UUID uuid) {
        return ResponseEntity.ok(service.findByUuid(uuid));
    }

    @PostMapping
    public ResponseEntity<CustomerResponse> create(
            @Valid @RequestBody CreateCustomerRequest request) {
        CustomerResponse response = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
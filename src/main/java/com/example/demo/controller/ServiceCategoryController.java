package com.example.demo.controller;
import com.example.demo.dto.services.CreateServiceCategoryRequest;
import com.example.demo.dto.services.ServiceCategoryResponse;
import com.example.demo.dto.services.UpdateServiceCategoryRequest;
import com.example.demo.service.ServiceCategoryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

import java.util.List;

@RestController
@RequestMapping("/api/service-categories")
public class ServiceCategoryController {

    private final ServiceCategoryService service;

    public ServiceCategoryController(ServiceCategoryService service) {
        this.service = service;
    }


    // it return all child & parent
    @GetMapping
    public ResponseEntity<List<ServiceCategoryResponse>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<ServiceCategoryResponse> findByUuid(
            @NotNull @PathVariable UUID uuid) {
        return ResponseEntity.ok(service.findByUuid(uuid));
    }

    @PostMapping
    public ResponseEntity<ServiceCategoryResponse> create(
            @Valid @RequestBody CreateServiceCategoryRequest request) {
        ServiceCategoryResponse response = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<ServiceCategoryResponse> update(
            @NotNull
            @PathVariable UUID uuid,
            @Valid @RequestBody UpdateServiceCategoryRequest request) {
        return ResponseEntity.ok(service.update(uuid, request));
    }
}
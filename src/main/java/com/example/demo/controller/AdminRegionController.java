package com.example.demo.controller;


import com.example.demo.dto.region.CreateRegionRequest;
import com.example.demo.dto.region.RegionResponse;
import com.example.demo.dto.region.UpdateRegionRequest;
import com.example.demo.service.RegionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/admin/regions")
public class AdminRegionController {

    private final RegionService service;

    public AdminRegionController(RegionService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<RegionResponse> create(
            @Valid @RequestBody CreateRegionRequest request
    ) {
        RegionResponse response = service.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<RegionResponse> update(
            @PathVariable UUID uuid,
            @Valid @RequestBody UpdateRegionRequest request
    ) {
        return ResponseEntity.ok(
                service.update(uuid, request)
        );
    }

    @PatchMapping("/{uuid}/enable")
    public ResponseEntity<Void> enable(
            @PathVariable UUID uuid
    ) {
        service.enable(uuid);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{uuid}/disable")
    public ResponseEntity<Void> disable(
            @PathVariable UUID uuid
    ) {
        service.disable(uuid);
        return ResponseEntity.noContent().build();
    }
}

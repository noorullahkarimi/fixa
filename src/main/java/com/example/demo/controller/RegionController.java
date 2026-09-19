package com.example.demo.controller;

import com.example.demo.dto.region.RegionResponse;
import com.example.demo.service.RegionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/regions")
public class RegionController {

    private final RegionService service;

    public RegionController(RegionService service) {
        this.service = service;
    }


    // take all region root
    @GetMapping
    public ResponseEntity<List<RegionResponse>> findAllParentRegion() {
        return ResponseEntity.ok(service.findAll());
    }

    // take a region by uuid
    @GetMapping("/{uuid}")
    public ResponseEntity<RegionResponse> findByUuid(
            @PathVariable UUID uuid
    ) {
        return ResponseEntity.ok(service.findByUuid(uuid));
    }

    //take all child region by uuid
    @GetMapping("/{uuid}/children")
    public ResponseEntity<List<RegionResponse>> findChildren(
            @PathVariable UUID uuid
    ) {
        return ResponseEntity.ok(service.findChildren(uuid));
    }
}


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

    @GetMapping
    public ResponseEntity<List<RegionResponse>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<RegionResponse> findByUuid(
            @PathVariable UUID uuid
    ) {
        return ResponseEntity.ok(service.findByUuid(uuid));
    }

    @GetMapping("/{uuid}/children")
    public ResponseEntity<List<RegionResponse>> findChildren(
            @PathVariable UUID uuid
    ) {
        return ResponseEntity.ok(service.findChildren(uuid));
    }
}


//
//import com.example.demo.dto.region.CreateRegionRequest;
//import com.example.demo.dto.region.RegionResponse;
//import com.example.demo.dto.region.UpdateRegionRequest;
//import com.example.demo.service.RegionService;
//import jakarta.validation.Valid;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/regions")
//public class RegionController {
//
//    private final RegionService service;
//
//    public RegionController(RegionService service) {
//        this.service = service;
//    }
//
//    @GetMapping
//    public ResponseEntity<List<RegionResponse>> findAll() {
//        return ResponseEntity.ok(service.findAll());
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<RegionResponse> findById(@PathVariable Long id) {
//        return ResponseEntity.ok(service.findById(id));
//    }
//
//    @PostMapping
//    public ResponseEntity<RegionResponse> create(
//            @Valid @RequestBody CreateRegionRequest request) {
//        RegionResponse response = service.create(request);
//        return ResponseEntity.status(HttpStatus.CREATED).body(response);
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<RegionResponse> update(
//            @PathVariable Long id,
//            @Valid @RequestBody UpdateRegionRequest request) {
//        return ResponseEntity.ok(service.update(id, request));
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> softDelete(@PathVariable Long id) {
//        service.softDelete(id);
//        return ResponseEntity.noContent().build();
//    }
//}

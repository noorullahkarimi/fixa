package com.example.demo.controller;
import com.example.demo.dto.order.*;
import com.example.demo.service.OrderService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> findById(@NotNull @PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }


    @PostMapping
    public ResponseEntity<CreateOrderResponse> create(
            @Valid @RequestBody CreateOrderRequest request) {
        CreateOrderResponse response = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderResponse> changeStatus(@NotNull
            @PathVariable Long id,
            @Valid @RequestBody ChangeOrderStatusRequest request) {
        return ResponseEntity.ok(service.changeStatus(id, request));
    }


    @GetMapping("/{id}/status-history")
    public ResponseEntity<List<OrderStatusHistoryResponse>> getStatusHistory(
            @NotNull
            @PathVariable Long id) {
        return ResponseEntity.ok(service.getStatusHistory(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> softDelete(@PathVariable Long id) {
        service.softDelete(id);
        return ResponseEntity.noContent().build();
    }
}
package com.example.demo.controller;
import com.example.demo.dto.order.*;
import com.example.demo.service.OrderService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    // it should take userid, then shows its own orders
    @GetMapping("/{uuid}")
    public ResponseEntity<OrderResponse> findByUuid(
            @NotNull @PathVariable UUID uuid) {
        return ResponseEntity.ok(service.findByUuid(uuid));
    }


    @PostMapping
    public ResponseEntity<CreateOrderResponse> create(
            @Valid @RequestBody CreateOrderRequest request) {
        CreateOrderResponse response = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    // change staus order
    @PatchMapping("/{uuid}/status")
    public ResponseEntity<OrderResponse> changeStatus(
            @NotNull @PathVariable UUID uuid,
            @Valid @RequestBody ChangeOrderStatusRequest request) {
        return ResponseEntity.ok(service.changeStatus(uuid, request));
    }

    @GetMapping("/{uuid}/status-history")
    public ResponseEntity<List<OrderStatusHistoryResponse>> getStatusHistory(
            @NotNull @PathVariable UUID uuid) {
        return ResponseEntity.ok(service.getStatusHistory(uuid));
    }

}
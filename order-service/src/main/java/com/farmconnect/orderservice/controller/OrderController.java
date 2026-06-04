package com.farmconnect.orderservice.controller;

import com.farmconnect.orderservice.dto.*;
import com.farmconnect.orderservice.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponse>> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        OrderResponse response = orderService.createOrder(request);
        return new ResponseEntity<>(ApiResponse.success("Order created successfully", HttpStatus.CREATED.value(), response), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getAllOrders() {
        List<OrderResponse> response = orderService.getAllOrders();
        return ResponseEntity.ok(ApiResponse.success("Orders fetched successfully", HttpStatus.OK.value(), response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrderById(@PathVariable Long id) {
        OrderResponse response = orderService.getOrderById(id);
        return ResponseEntity.ok(ApiResponse.success("Order fetched successfully", HttpStatus.OK.value(), response));
    }

    @GetMapping("/buyer/{buyerId}")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getOrdersByBuyer(@PathVariable Long buyerId) {
        List<OrderResponse> response = orderService.getOrdersByBuyer(buyerId);
        return ResponseEntity.ok(ApiResponse.success("Buyer orders fetched successfully", HttpStatus.OK.value(), response));
    }

    @GetMapping("/farmer/{farmerId}")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getOrdersByFarmer(@PathVariable Long farmerId) {
        List<OrderResponse> response = orderService.getOrdersByFarmer(farmerId);
        return ResponseEntity.ok(ApiResponse.success("Farmer orders fetched successfully", HttpStatus.OK.value(), response));
    }

    @GetMapping("/track/{id}")
    public ResponseEntity<ApiResponse<OrderTrackingResponse>> trackOrder(@PathVariable Long id) {
        OrderTrackingResponse response = orderService.trackOrder(id);
        return ResponseEntity.ok(ApiResponse.success("Order tracking details fetched", HttpStatus.OK.value(), response));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<OrderResponse>> updateOrderStatus(@PathVariable Long id, @Valid @RequestBody UpdateOrderStatusRequest request) {
        OrderResponse response = orderService.updateOrderStatus(id, request);
        return ResponseEntity.ok(ApiResponse.success("Order status updated successfully", HttpStatus.OK.value(), response));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<OrderResponse>> cancelOrder(@PathVariable Long id) {
        OrderResponse response = orderService.cancelOrder(id);
        return ResponseEntity.ok(ApiResponse.success("Order cancelled successfully", HttpStatus.OK.value(), response));
    }
}

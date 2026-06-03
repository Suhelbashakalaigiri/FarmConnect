package com.farmconnect.logisticsservice.controller;

import com.farmconnect.logisticsservice.dto.*;
import com.farmconnect.logisticsservice.service.LogisticsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/logistics")
@RequiredArgsConstructor
public class LogisticsController {

    private final LogisticsService logisticsService;

    @PostMapping
    public ResponseEntity<ApiResponse<LogisticsResponse>> schedulePickup(@Valid @RequestBody CreateLogisticsRequest request) {
        LogisticsResponse response = logisticsService.schedulePickup(request);
        return new ResponseEntity<>(ApiResponse.success("Pickup scheduled successfully", HttpStatus.CREATED.value(), response), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<LogisticsResponse>>> getAllLogistics() {
        List<LogisticsResponse> response = logisticsService.getAllLogistics();
        return ResponseEntity.ok(ApiResponse.success("Logistics records fetched successfully", HttpStatus.OK.value(), response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<LogisticsResponse>> getLogisticsById(@PathVariable Long id) {
        LogisticsResponse response = logisticsService.getLogisticsById(id);
        return ResponseEntity.ok(ApiResponse.success("Logistics record fetched successfully", HttpStatus.OK.value(), response));
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<ApiResponse<LogisticsResponse>> getLogisticsByOrderId(@PathVariable Long orderId) {
        LogisticsResponse response = logisticsService.getLogisticsByOrderId(orderId);
        return ResponseEntity.ok(ApiResponse.success("Order logistics fetched successfully", HttpStatus.OK.value(), response));
    }

    @PutMapping("/{id}/assign-vehicle")
    public ResponseEntity<ApiResponse<LogisticsResponse>> assignVehicle(@PathVariable Long id, @Valid @RequestBody AssignVehicleRequest request) {
        LogisticsResponse response = logisticsService.assignVehicle(id, request);
        return ResponseEntity.ok(ApiResponse.success("Vehicle assigned successfully", HttpStatus.OK.value(), response));
    }

    @PutMapping("/{id}/arrived")
    public ResponseEntity<ApiResponse<LogisticsResponse>> markVehicleArrived(@PathVariable Long id) {
        LogisticsResponse response = logisticsService.markVehicleArrived(id);
        return ResponseEntity.ok(ApiResponse.success("Vehicle arrival marked successfully", HttpStatus.OK.value(), response));
    }

    @PutMapping("/{id}/tracking-status")
    public ResponseEntity<ApiResponse<LogisticsResponse>> updateTrackingStatus(@PathVariable Long id, @Valid @RequestBody UpdateTrackingStatusRequest request) {
        LogisticsResponse response = logisticsService.updateTrackingStatus(id, request);
        return ResponseEntity.ok(ApiResponse.success("Tracking status updated successfully", HttpStatus.OK.value(), response));
    }

    @GetMapping("/track/{orderId}")
    public ResponseEntity<ApiResponse<LogisticsTrackingResponse>> trackShipment(@PathVariable Long orderId) {
        LogisticsTrackingResponse response = logisticsService.trackShipment(orderId);
        return ResponseEntity.ok(ApiResponse.success("Tracking details fetched successfully", HttpStatus.OK.value(), response));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<LogisticsResponse>> cancelLogistics(@PathVariable Long id) {
        LogisticsResponse response = logisticsService.cancelLogistics(id);
        return ResponseEntity.ok(ApiResponse.success("Logistics cancelled successfully", HttpStatus.OK.value(), response));
    }
}

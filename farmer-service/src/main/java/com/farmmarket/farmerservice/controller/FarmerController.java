package com.farmmarket.farmerservice.controller;

import com.farmmarket.farmerservice.dto.*;
import com.farmmarket.farmerservice.service.FarmerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/farmers")
@RequiredArgsConstructor
public class FarmerController {

    private final FarmerService farmerService;

    @PostMapping
    public ResponseEntity<ApiResponse<FarmerResponse>> registerFarmer(@Valid @RequestBody CreateFarmerRequest request) {
        FarmerResponse response = farmerService.createFarmer(request);
        return new ResponseEntity<>(
                ApiResponse.success("Farmer registered successfully", HttpStatus.CREATED.value(), response),
                HttpStatus.CREATED
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<FarmerResponse>>> getAllFarmers() {
        List<FarmerResponse> response = farmerService.getAllFarmers();
        return ResponseEntity.ok(ApiResponse.success("Farmers retrieved successfully", HttpStatus.OK.value(), response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<FarmerResponse>> getFarmerById(@PathVariable Long id) {
        FarmerResponse response = farmerService.getFarmerById(id);
        return ResponseEntity.ok(ApiResponse.success("Farmer retrieved successfully", HttpStatus.OK.value(), response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<FarmerResponse>> updateFarmer(
            @PathVariable Long id,
            @Valid @RequestBody UpdateFarmerRequest request) {
        FarmerResponse response = farmerService.updateFarmer(id, request);
        return ResponseEntity.ok(ApiResponse.success("Farmer profile updated successfully", HttpStatus.OK.value(), response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteFarmer(@PathVariable Long id) {
        farmerService.deleteFarmer(id);
        return ResponseEntity.ok(ApiResponse.success("Farmer deleted successfully", HttpStatus.OK.value(), null));
    }

    @GetMapping("/{id}/crops")
    public ResponseEntity<ApiResponse<List<CropSummary>>> getFarmerCropSummary(@PathVariable Long id) {
        List<CropSummary> response = farmerService.getFarmerCropSummary(id);
        return ResponseEntity.ok(ApiResponse.success("Farmer crop summary retrieved successfully", HttpStatus.OK.value(), response));
    }

    @GetMapping("/{id}/orders")
    public ResponseEntity<ApiResponse<List<OrderSummary>>> getFarmerOrdersSummary(@PathVariable Long id) {
        List<OrderSummary> response = farmerService.getFarmerOrdersSummary(id);
        return ResponseEntity.ok(ApiResponse.success("Farmer orders summary retrieved successfully", HttpStatus.OK.value(), response));
    }
}

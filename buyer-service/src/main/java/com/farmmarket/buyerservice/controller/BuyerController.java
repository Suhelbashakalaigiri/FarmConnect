package com.farmmarket.buyerservice.controller;

import com.farmmarket.buyerservice.dto.*;
import com.farmmarket.buyerservice.service.BuyerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/buyers")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BuyerController {

    private final BuyerService buyerService;

    @PostMapping("/internal/create-profile")
    public ResponseEntity<ApiResponse<BuyerResponse>> createInternalProfile(@Valid @RequestBody CreateBuyerProfileRequest request) {
        BuyerResponse response = buyerService.createInternalProfile(request);
        return new ResponseEntity<>(ApiResponse.success("Internal buyer profile created", HttpStatus.CREATED.value(), response), HttpStatus.CREATED);
    }

    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<BuyerResponse>> completeProfile(
            @RequestParam Long id,
            @Valid @RequestBody BuyerProfileUpdateRequest request) {
        BuyerResponse response = buyerService.completeProfile(id, request);
        return ResponseEntity.ok(ApiResponse.success("Profile updated successfully", HttpStatus.OK.value(), response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<BuyerResponse>>> getAllBuyers() {
        List<BuyerResponse> response = buyerService.getAllBuyers();
        return ResponseEntity.ok(ApiResponse.success("Buyers fetched successfully", HttpStatus.OK.value(), response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BuyerResponse>> getBuyerById(@PathVariable Long id) {
        BuyerResponse response = buyerService.getBuyerById(id);
        return ResponseEntity.ok(ApiResponse.success("Buyer fetched successfully", HttpStatus.OK.value(), response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BuyerResponse>> updateBuyer(@PathVariable Long id, @Valid @RequestBody BuyerRequest request) {
        BuyerResponse response = buyerService.updateBuyer(id, request);
        return ResponseEntity.ok(ApiResponse.success("Buyer updated successfully", HttpStatus.OK.value(), response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteBuyer(@PathVariable Long id) {
        buyerService.deleteBuyer(id);
        return ResponseEntity.ok(ApiResponse.success("Buyer deleted successfully", HttpStatus.OK.value(), null));
    }



    @GetMapping("/crops")
    public ResponseEntity<ApiResponse<List<CropResponse>>> browseCrops() {
        List<CropResponse> response = buyerService.browseCrops();
        return ResponseEntity.ok(ApiResponse.success("Crops fetched successfully", HttpStatus.OK.value(), response));
    }

    @GetMapping("/crops/{cropId}")
    public ResponseEntity<ApiResponse<CropResponse>> getCropById(@PathVariable Long cropId) {
        CropResponse response = buyerService.getCropById(cropId);
        return ResponseEntity.ok(ApiResponse.success("Crop details fetched successfully", HttpStatus.OK.value(), response));
    }



    @PostMapping("/bids")
    public ResponseEntity<ApiResponse<String>> placeBid(@Valid @RequestBody BidRequest request) {
        String message = buyerService.placeBid(request);
        return ResponseEntity.ok(ApiResponse.success(message, HttpStatus.OK.value(), null));
    }


    @PostMapping("/orders")
    @Deprecated
    public ResponseEntity<ApiResponse<OrderResponse>> placeOrder(@Valid @RequestBody OrderRequest request) {
        buyerService.placeOrder(request);
        return null; // unreachable due to exception in service
    }

    @PostMapping("/visits/{visitId}/approve")
    public ResponseEntity<ApiResponse<String>> approveVisit(@PathVariable Long visitId) {
        String message = buyerService.approveVisit(visitId);
        return ResponseEntity.ok(ApiResponse.success(message, HttpStatus.OK.value(), null));
    }

    @PostMapping("/visits/{visitId}/reject")
    public ResponseEntity<ApiResponse<String>> rejectVisit(@PathVariable Long visitId) {
        String message = buyerService.rejectVisit(visitId);
        return ResponseEntity.ok(ApiResponse.success(message, HttpStatus.OK.value(), null));
    }



    @GetMapping("/deliveries/{orderId}")
    public ResponseEntity<ApiResponse<DeliveryResponse>> trackDelivery(@PathVariable String orderId) {
        DeliveryResponse response = buyerService.trackDelivery(orderId);
        return ResponseEntity.ok(ApiResponse.success("Delivery status fetched successfully", HttpStatus.OK.value(), response));
    }



    @GetMapping("/transactions")
    public ResponseEntity<ApiResponse<List<TransactionResponse>>> getTransactionHistory() {
        List<TransactionResponse> response = buyerService.getTransactionHistory();
        return ResponseEntity.ok(ApiResponse.success("Transaction history fetched successfully", HttpStatus.OK.value(), response));
    }
}

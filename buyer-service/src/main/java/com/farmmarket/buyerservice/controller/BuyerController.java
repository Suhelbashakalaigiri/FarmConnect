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
public class BuyerController {

    private final BuyerService buyerService;

    // --- Buyer Profile APIs ---

    @PostMapping
    public ResponseEntity<ApiResponse<BuyerResponse>> registerBuyer(@Valid @RequestBody BuyerRequest request) {
        BuyerResponse response = buyerService.registerBuyer(request);
        return new ResponseEntity<>(ApiResponse.success("Buyer registered successfully", HttpStatus.CREATED.value(), response), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<BuyerResponse>>> getAllBuyers() {
        List<BuyerResponse> response = buyerService.getAllBuyers();
        return ResponseEntity.ok(ApiResponse.success("Buyers fetched successfully", HttpStatus.OK.value(), response));
    }

    @GetMapping("/{buyerId}")
    public ResponseEntity<ApiResponse<BuyerResponse>> getBuyerById(@PathVariable Long buyerId) {
        BuyerResponse response = buyerService.getBuyerById(buyerId);
        return ResponseEntity.ok(ApiResponse.success("Buyer fetched successfully", HttpStatus.OK.value(), response));
    }

    @PutMapping("/{buyerId}")
    public ResponseEntity<ApiResponse<BuyerResponse>> updateBuyer(@PathVariable Long buyerId, @Valid @RequestBody BuyerRequest request) {
        BuyerResponse response = buyerService.updateBuyer(buyerId, request);
        return ResponseEntity.ok(ApiResponse.success("Buyer updated successfully", HttpStatus.OK.value(), response));
    }

    @DeleteMapping("/{buyerId}")
    public ResponseEntity<ApiResponse<String>> deleteBuyer(@PathVariable Long buyerId) {
        buyerService.deleteBuyer(buyerId);
        return ResponseEntity.ok(ApiResponse.success("Buyer deleted successfully", HttpStatus.OK.value(), null));
    }

    // --- Mock Crop Browsing APIs ---

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

    // --- Mock Bid APIs ---

    @PostMapping("/bids")
    public ResponseEntity<ApiResponse<String>> placeBid(@Valid @RequestBody BidRequest request) {
        String message = buyerService.placeBid(request);
        return ResponseEntity.ok(ApiResponse.success(message, HttpStatus.OK.value(), null));
    }

    // --- Mock Order APIs ---

    @PostMapping("/orders")
    public ResponseEntity<ApiResponse<OrderResponse>> placeOrder(@Valid @RequestBody OrderRequest request) {
        OrderResponse response = buyerService.placeOrder(request);
        return ResponseEntity.ok(ApiResponse.success("Order placed successfully", HttpStatus.OK.value(), response));
    }

    // --- Mock Delivery APIs ---

    @GetMapping("/deliveries/{orderId}")
    public ResponseEntity<ApiResponse<DeliveryResponse>> trackDelivery(@PathVariable String orderId) {
        DeliveryResponse response = buyerService.trackDelivery(orderId);
        return ResponseEntity.ok(ApiResponse.success("Delivery status fetched successfully", HttpStatus.OK.value(), response));
    }

    // --- Mock Transaction APIs ---

    @GetMapping("/transactions")
    public ResponseEntity<ApiResponse<List<TransactionResponse>>> getTransactionHistory() {
        List<TransactionResponse> response = buyerService.getTransactionHistory();
        return ResponseEntity.ok(ApiResponse.success("Transaction history fetched successfully", HttpStatus.OK.value(), response));
    }
}

package com.farmmarket.biddingservice.controller;

import com.farmmarket.biddingservice.dto.*;
import com.farmmarket.biddingservice.service.BidService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bids")
@RequiredArgsConstructor
public class BidController {

    private final BidService bidService;

    @PostMapping
    public ResponseEntity<ApiResponse<BidResponse>> createBid(@Valid @RequestBody CreateBidRequest request) {
        BidResponse response = bidService.createBid(request);
        return new ResponseEntity<>(ApiResponse.success("Bid placed successfully", HttpStatus.CREATED.value(), response), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<BidResponse>>> getAllBids() {
        List<BidResponse> response = bidService.getAllBids();
        return ResponseEntity.ok(ApiResponse.success("Bids fetched successfully", HttpStatus.OK.value(), response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BidResponse>> getBidById(@PathVariable Long id) {
        BidResponse response = bidService.getBidById(id);
        return ResponseEntity.ok(ApiResponse.success("Bid fetched successfully", HttpStatus.OK.value(), response));
    }

    @GetMapping("/crop/{cropId}")
    public ResponseEntity<ApiResponse<List<BidResponse>>> getBidsByCrop(@PathVariable Long cropId) {
        List<BidResponse> response = bidService.getBidsByCrop(cropId);
        return ResponseEntity.ok(ApiResponse.success("Bids for crop fetched successfully", HttpStatus.OK.value(), response));
    }

    @GetMapping("/buyer/{buyerId}")
    public ResponseEntity<ApiResponse<List<BidResponse>>> getBidsByBuyer(@PathVariable Long buyerId) {
        List<BidResponse> response = bidService.getBidsByBuyer(buyerId);
        return ResponseEntity.ok(ApiResponse.success("Bids for buyer fetched successfully", HttpStatus.OK.value(), response));
    }

    @GetMapping("/crop/{cropId}/highest")
    public ResponseEntity<ApiResponse<HighestBidResponse>> getHighestBid(@PathVariable Long cropId) {
        HighestBidResponse response = bidService.getHighestBid(cropId);
        return ResponseEntity.ok(ApiResponse.success("Highest bid fetched successfully", HttpStatus.OK.value(), response));
    }

    @PutMapping("/{id}/accept")
    public ResponseEntity<ApiResponse<BidResponse>> acceptBid(@PathVariable Long id) {
        BidResponse response = bidService.acceptBid(id);
        return ResponseEntity.ok(ApiResponse.success("Bid accepted successfully", HttpStatus.OK.value(), response));
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<ApiResponse<BidResponse>> rejectBid(@PathVariable Long id) {
        BidResponse response = bidService.rejectBid(id);
        return ResponseEntity.ok(ApiResponse.success("Bid rejected successfully", HttpStatus.OK.value(), response));
    }

    @PutMapping("/{id}/withdraw")
    public ResponseEntity<ApiResponse<BidResponse>> withdrawBid(@PathVariable Long id) {
        BidResponse response = bidService.withdrawBid(id);
        return ResponseEntity.ok(ApiResponse.success("Bid withdrawn successfully", HttpStatus.OK.value(), response));
    }
}

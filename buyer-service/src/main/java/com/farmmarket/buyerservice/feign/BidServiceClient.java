package com.farmmarket.buyerservice.feign;

import com.farmmarket.buyerservice.dto.ApiResponse;
import com.farmmarket.buyerservice.dto.BidCreateRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "BIDDING-SERVICE")
public interface BidServiceClient {
    @PostMapping("/api/bids")
    ApiResponse<Object> createBid(@RequestBody BidCreateRequest request);
}

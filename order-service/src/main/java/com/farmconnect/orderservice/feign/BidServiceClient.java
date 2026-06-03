package com.farmconnect.orderservice.feign;

import com.farmconnect.orderservice.dto.ApiResponse;
import com.farmconnect.orderservice.dto.ExternalBidResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "BIDDING-SERVICE")
public interface BidServiceClient {
    @GetMapping("/api/bids/{id}")
    ApiResponse<ExternalBidResponse> getBidById(@PathVariable("id") Long id);

    @PutMapping("/api/bids/{id}/status")
    ApiResponse<Object> updateBidStatus(@PathVariable("id") Long id, @RequestParam("status") String status);
}

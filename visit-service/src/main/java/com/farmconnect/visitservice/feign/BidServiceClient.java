package com.farmconnect.visitservice.feign;

import com.farmconnect.visitservice.dto.ApiResponse;
import com.farmconnect.visitservice.dto.ExternalBidResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "BIDDING-SERVICE")
public interface BidServiceClient {
    @GetMapping("/api/bids/{id}")
    ApiResponse<ExternalBidResponse> getBidById(@PathVariable("id") Long id);
}

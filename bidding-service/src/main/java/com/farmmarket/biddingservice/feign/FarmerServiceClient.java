package com.farmmarket.biddingservice.feign;

import com.farmmarket.biddingservice.dto.ApiResponse;
import com.farmmarket.biddingservice.dto.ExternalFarmerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "FARMER-SERVICE")
public interface FarmerServiceClient {
    @GetMapping("/api/farmers/{farmerId}")
    ApiResponse<ExternalFarmerResponse> getFarmerById(@PathVariable("farmerId") Long farmerId);
}

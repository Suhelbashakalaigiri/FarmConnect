package com.farmconnect.authservice.feign.client;

import com.farmconnect.authservice.dto.ApiResponse;
import com.farmconnect.authservice.feign.dto.CreateFarmerProfileRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "FARMER-SERVICE")
public interface FarmerServiceClient {
    @PostMapping("/api/farmers/internal/create-profile")
    ApiResponse<Object> createFarmerProfile(@RequestBody CreateFarmerProfileRequest request);
}

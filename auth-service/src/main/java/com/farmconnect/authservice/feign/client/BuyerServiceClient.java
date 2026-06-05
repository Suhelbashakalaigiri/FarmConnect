package com.farmconnect.authservice.feign.client;

import com.farmconnect.authservice.dto.ApiResponse;
import com.farmconnect.authservice.feign.dto.CreateBuyerProfileRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "BUYER-SERVICE")
public interface BuyerServiceClient {
    @PostMapping("/api/buyers/internal/create-profile")
    ApiResponse<Object> createBuyerProfile(@RequestBody CreateBuyerProfileRequest request);
}

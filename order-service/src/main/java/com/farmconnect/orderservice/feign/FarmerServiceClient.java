package com.farmconnect.orderservice.feign;

import com.farmconnect.orderservice.dto.ApiResponse;
import com.farmconnect.orderservice.dto.ExternalFarmerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "FARMER-SERVICE")
public interface FarmerServiceClient {
    @GetMapping("/api/farmers/{id}")
    ApiResponse<ExternalFarmerResponse> getFarmerById(@PathVariable("id") Long id);
}

package com.farmconnect.crop.feign;

import com.farmconnect.crop.dto.ApiResponse;
import com.farmconnect.crop.dto.ExternalFarmerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "FARMER-SERVICE")
public interface FarmerServiceClient {
    @GetMapping("/api/farmers/{id}")
    ApiResponse<ExternalFarmerResponse> getFarmerById(@PathVariable("id") Long id);
}

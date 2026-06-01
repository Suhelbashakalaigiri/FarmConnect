package com.farmmarket.biddingservice.feign;

import com.farmmarket.biddingservice.dto.ApiResponse;
import com.farmmarket.biddingservice.dto.ExternalCropResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "CROP-SERVICE")
public interface CropServiceClient {
    @GetMapping("/api/crop/getcropby/{cropId}")
    ApiResponse<ExternalCropResponse> getCropById(@PathVariable("cropId") Long cropId);
}

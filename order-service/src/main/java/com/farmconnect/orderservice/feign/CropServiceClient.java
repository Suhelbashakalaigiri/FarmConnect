package com.farmconnect.orderservice.feign;

import com.farmconnect.orderservice.dto.ApiResponse;
import com.farmconnect.orderservice.dto.ExternalCropResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "CROP-SERVICE")
public interface CropServiceClient {
    @GetMapping("/api/crop/getcropby/{id}")
    ApiResponse<ExternalCropResponse> getCropById(@PathVariable("id") Long id);

    @PutMapping("/api/crop/{cropId}/reduce-quantity")
    ApiResponse<Object> reduceQuantity(@PathVariable("cropId") Long cropId, @RequestParam("quantity") Double quantity);
}

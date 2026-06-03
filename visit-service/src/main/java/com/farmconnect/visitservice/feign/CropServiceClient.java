package com.farmconnect.visitservice.feign;

import com.farmconnect.visitservice.dto.ApiResponse;
import com.farmconnect.visitservice.dto.ExternalCropResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "CROP-SERVICE")
public interface CropServiceClient {
    @GetMapping("/api/crop/getcropby/{id}")
    ApiResponse<ExternalCropResponse> getCropById(@PathVariable("id") Long id);
}

package com.farmmarket.buyerservice.feign;

import com.farmmarket.buyerservice.dto.ApiResponse;
import com.farmmarket.buyerservice.dto.CropResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "CROP-SERVICE")
public interface CropServiceClient {
    @GetMapping("/api/crop/getallcrops")
    ApiResponse<List<CropResponse>> getAllCrops();

    @GetMapping("/api/crop/getcropby/{Id}")
    ApiResponse<CropResponse> getCropById(@PathVariable("Id") Long Id);
}

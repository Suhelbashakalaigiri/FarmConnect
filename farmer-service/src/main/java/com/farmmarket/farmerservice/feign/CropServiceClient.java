package com.farmmarket.farmerservice.feign;

import com.farmmarket.farmerservice.dto.ApiResponse;
import com.farmmarket.farmerservice.dto.CropSummary;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "CROP-SERVICE")
public interface CropServiceClient {
    @GetMapping("/api/crop/farmer/{farmerId}")
    ApiResponse<List<CropSummary>> getCropsByFarmerId(@PathVariable("farmerId") Long farmerId);
}

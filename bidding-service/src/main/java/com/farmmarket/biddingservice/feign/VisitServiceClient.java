package com.farmmarket.biddingservice.feign;

import com.farmmarket.biddingservice.dto.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "VISIT-SERVICE")
public interface VisitServiceClient {

    @PostMapping("/api/visits")
    ApiResponse<Object> createVisitRequest(
            @RequestParam("bidId") Long bidId,
            @RequestParam("farmerId") Long farmerId,
            @RequestParam("buyerId") Long buyerId,
            @RequestParam("cropId") Long cropId
    );
}

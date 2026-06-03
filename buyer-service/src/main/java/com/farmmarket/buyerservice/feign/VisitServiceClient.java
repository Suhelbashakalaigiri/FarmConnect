package com.farmmarket.buyerservice.feign;

import com.farmmarket.buyerservice.dto.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "VISIT-SERVICE")
public interface VisitServiceClient {

    @PostMapping("/api/visits/{visitId}/approve")
    ApiResponse<Object> approveVisit(@PathVariable("visitId") Long visitId);

    @PostMapping("/api/visits/{visitId}/reject")
    ApiResponse<Object> rejectVisit(@PathVariable("visitId") Long visitId);
}

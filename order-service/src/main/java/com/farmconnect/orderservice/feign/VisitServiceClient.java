package com.farmconnect.orderservice.feign;

import com.farmconnect.orderservice.dto.ApiResponse;
import com.farmconnect.orderservice.dto.ExternalVisitResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "VISIT-SERVICE")
public interface VisitServiceClient {
    @GetMapping("/api/visits/{id}")
    ApiResponse<ExternalVisitResponse> getVisitById(@PathVariable("id") Long id);
}

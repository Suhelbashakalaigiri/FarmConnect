package com.farmconnect.orderservice.feign;

import com.farmconnect.orderservice.dto.ApiResponse;
import com.farmconnect.orderservice.dto.ExternalBuyerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "BUYER-SERVICE")
public interface BuyerServiceClient {
    @GetMapping("/api/buyers/{buyerId}")
    ApiResponse<ExternalBuyerResponse> getBuyerById(@PathVariable("buyerId") Long buyerId);
}

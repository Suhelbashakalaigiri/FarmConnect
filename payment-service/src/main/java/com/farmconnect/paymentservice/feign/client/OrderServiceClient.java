package com.farmconnect.paymentservice.feign.client;

import com.farmconnect.paymentservice.dto.ApiResponse;
import com.farmconnect.paymentservice.feign.dto.OrderServiceResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ORDER-SERVICE")
public interface OrderServiceClient {
    @GetMapping("/api/orders/{orderId}")
    ApiResponse<OrderServiceResponse> getOrderById(@PathVariable("orderId") Long orderId);
}

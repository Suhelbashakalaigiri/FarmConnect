package com.farmconnect.logisticsservice.feign.client;

import com.farmconnect.logisticsservice.dto.ApiResponse;
import com.farmconnect.logisticsservice.feign.dto.OrderServiceResponse;
import com.farmconnect.logisticsservice.feign.dto.OrderStatusUpdateRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "ORDER-SERVICE")
public interface OrderServiceClient {
    @GetMapping("/api/orders/{id}")
    ApiResponse<OrderServiceResponse> getOrderById(@PathVariable("id") Long id);

    @PutMapping("/api/orders/{id}/status")
    ApiResponse<Object> updateOrderStatus(@PathVariable("id") Long id, @RequestBody OrderStatusUpdateRequest request);
}

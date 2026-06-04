package com.farmconnect.paymentservice.feign.client;

import com.farmconnect.paymentservice.dto.ApiResponse;
import com.farmconnect.paymentservice.feign.dto.LogisticsServiceResponse;
import com.farmconnect.paymentservice.feign.dto.LogisticsStatusUpdateRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "LOGISTICS-SERVICE")
public interface LogisticsServiceClient {
    @GetMapping("/api/logistics/order/{orderId}")
    ApiResponse<LogisticsServiceResponse> getLogisticsByOrderId(@PathVariable("orderId") Long orderId);

    @PutMapping("/api/logistics/{id}/tracking-status")
    ApiResponse<Object> updateTrackingStatus(@PathVariable("id") Long id, @RequestBody LogisticsStatusUpdateRequest request);
}

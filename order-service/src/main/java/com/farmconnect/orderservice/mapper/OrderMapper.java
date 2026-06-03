package com.farmconnect.orderservice.mapper;

import com.farmconnect.orderservice.dto.OrderResponse;
import com.farmconnect.orderservice.dto.OrderSummaryResponse;
import com.farmconnect.orderservice.dto.OrderTrackingResponse;
import com.farmconnect.orderservice.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMapper {
    OrderResponse toResponse(Order order);
    OrderSummaryResponse toSummary(Order order);
    OrderTrackingResponse toTracking(Order order);
    List<OrderResponse> toResponseList(List<Order> orders);
}

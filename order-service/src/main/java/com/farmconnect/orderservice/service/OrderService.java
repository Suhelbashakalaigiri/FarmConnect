package com.farmconnect.orderservice.service;

import com.farmconnect.orderservice.dto.*;

import java.util.List;

public interface OrderService {
    OrderResponse createOrder(CreateOrderRequest request);
    OrderResponse getOrderById(Long id);
    List<OrderResponse> getAllOrders();
    List<OrderResponse> getOrdersByBuyer(Long buyerId);
    List<OrderResponse> getOrdersByFarmer(Long farmerId);
    OrderResponse updateOrderStatus(Long id, UpdateOrderStatusRequest request);
    OrderResponse cancelOrder(Long id);
    OrderTrackingResponse trackOrder(Long id);
}

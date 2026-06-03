package com.farmconnect.orderservice.service.impl;

import com.farmconnect.orderservice.dto.*;
import com.farmconnect.orderservice.entity.Order;
import com.farmconnect.orderservice.enums.OrderStatus;
import com.farmconnect.orderservice.exception.*;
import com.farmconnect.orderservice.feign.*;
import com.farmconnect.orderservice.mapper.OrderMapper;
import com.farmconnect.orderservice.repository.OrderRepository;
import com.farmconnect.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final VisitServiceClient visitServiceClient;
    private final BidServiceClient bidServiceClient;
    private final CropServiceClient cropServiceClient;
    private final BuyerServiceClient buyerServiceClient;
    private final FarmerServiceClient farmerServiceClient;

    @Override
    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request) {
        log.info("Creating order for bid: {} and visit: {}", request.bidId(), request.visitId());

        // 1. Prevent Duplicates
        if (orderRepository.existsByVisitId(request.visitId())) {
            throw new OrderAlreadyExistsException("Order already exists for visit ID: " + request.visitId());
        }
        if (orderRepository.existsByBidId(request.bidId())) {
            throw new OrderAlreadyExistsException("Order already exists for bid ID: " + request.bidId());
        }

        // 2. Validate Visit
        ApiResponse<ExternalVisitResponse> visitResp = visitServiceClient.getVisitById(request.visitId());
        if (!visitResp.success() || visitResp.data() == null) {
            throw new ResourceNotFoundException("Visit not found with ID: " + request.visitId());
        }
        if (!"APPROVED".equalsIgnoreCase(visitResp.data().inspectionStatus())) {
            throw new BusinessValidationException("Order can only be created for APPROVED inspections. Current: " + visitResp.data().inspectionStatus());
        }

        // 3. Validate Bid
        ApiResponse<ExternalBidResponse> bidResp = bidServiceClient.getBidById(request.bidId());
        if (!bidResp.success() || bidResp.data() == null) {
            throw new ResourceNotFoundException("Bid not found with ID: " + request.bidId());
        }
        if (!"INSPECTION_APPROVED".equalsIgnoreCase(bidResp.data().bidStatus())) {
            throw new BusinessValidationException("Bid must be in INSPECTION_APPROVED state.");
        }

        // 4. Validate Buyer
        ApiResponse<ExternalBuyerResponse> buyerResp = buyerServiceClient.getBuyerById(request.buyerId());
        if (!buyerResp.success() || buyerResp.data() == null || !"ACTIVE".equalsIgnoreCase(buyerResp.data().status())) {
            throw new BusinessValidationException("Buyer must be ACTIVE.");
        }

        // 5. Validate Farmer
        ApiResponse<ExternalFarmerResponse> farmerResp = farmerServiceClient.getFarmerById(request.farmerId());
        if (!farmerResp.success() || farmerResp.data() == null || !"ACTIVE".equalsIgnoreCase(farmerResp.data().status())) {
            throw new BusinessValidationException("Farmer must be ACTIVE.");
        }

        // 6. Validate Crop and Quantity
        ApiResponse<ExternalCropResponse> cropResp = cropServiceClient.getCropById(request.cropId());
        if (!cropResp.success() || cropResp.data() == null) {
            throw new ResourceNotFoundException("Crop not found with ID: " + request.cropId());
        }
        if (cropResp.data().quantity() < request.quantity()) {
            throw new BusinessValidationException("Insufficient crop quantity. Available: " + cropResp.data().quantity());
        }

        // 7. Calculate Amount
        BigDecimal unitPrice = bidResp.data().bidAmount();
        BigDecimal totalAmount = unitPrice.multiply(BigDecimal.valueOf(request.quantity()));

        // 8. Create Order
        Order order = Order.builder()
                .orderNumber("ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .visitId(request.visitId())
                .bidId(request.bidId())
                .cropId(request.cropId())
                .buyerId(request.buyerId())
                .farmerId(request.farmerId())
                .quantity(request.quantity())
                .unitPrice(unitPrice)
                .totalAmount(totalAmount)
                .orderStatus(OrderStatus.CREATED)
                .remarks(request.remarks())
                .build();

        Order savedOrder = orderRepository.save(order);

        // 9. Post-Creation Updates
        try {
            // Reduce Crop Quantity
            cropServiceClient.reduceQuantity(request.cropId(), request.quantity());
            
            // Update Bid Status
            bidServiceClient.updateBidStatus(request.bidId(), "ORDER_CREATED");
        } catch (Exception e) {
            log.error("Failed post-order updates: {}", e.getMessage());
            // In production, consider compensating transactions or a retry mechanism
        }

        return orderMapper.toResponse(savedOrder);
    }

    @Override
    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + id));
        return orderMapper.toResponse(order);
    }

    @Override
    public List<OrderResponse> getAllOrders() {
        return orderMapper.toResponseList(orderRepository.findAll());
    }

    @Override
    public List<OrderResponse> getOrdersByBuyer(Long buyerId) {
        return orderMapper.toResponseList(orderRepository.findByBuyerId(buyerId));
    }

    @Override
    public List<OrderResponse> getOrdersByFarmer(Long farmerId) {
        return orderMapper.toResponseList(orderRepository.findByFarmerId(farmerId));
    }

    @Override
    @Transactional
    public OrderResponse updateOrderStatus(Long id, UpdateOrderStatusRequest request) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + id));

        if (order.getOrderStatus() == OrderStatus.CANCELLED || order.getOrderStatus() == OrderStatus.DELIVERED) {
            throw new InvalidOrderStateException("Cannot update status of a " + order.getOrderStatus() + " order.");
        }

        order.setOrderStatus(request.orderStatus());
        if (request.remarks() != null) {
            order.setRemarks(request.remarks());
        }

        return orderMapper.toResponse(orderRepository.save(order));
    }

    @Override
    @Transactional
    public OrderResponse cancelOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + id));

        if (order.getOrderStatus() != OrderStatus.CREATED && order.getOrderStatus() != OrderStatus.CONFIRMED) {
            throw new InvalidOrderStateException("Order can only be cancelled in CREATED or CONFIRMED state.");
        }

        order.setOrderStatus(OrderStatus.CANCELLED);
        return orderMapper.toResponse(orderRepository.save(order));
    }

    @Override
    public OrderTrackingResponse trackOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + id));
        return orderMapper.toTracking(order);
    }
}

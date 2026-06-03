package com.farmconnect.logisticsservice.service.impl;

import com.farmconnect.logisticsservice.dto.*;
import com.farmconnect.logisticsservice.entity.Logistics;
import com.farmconnect.logisticsservice.enums.TrackingStatus;
import com.farmconnect.logisticsservice.exception.*;
import com.farmconnect.logisticsservice.feign.client.OrderServiceClient;
import com.farmconnect.logisticsservice.feign.dto.OrderServiceResponse;
import com.farmconnect.logisticsservice.feign.dto.OrderStatusUpdateRequest;
import com.farmconnect.logisticsservice.mapper.LogisticsMapper;
import com.farmconnect.logisticsservice.repository.LogisticsRepository;
import com.farmconnect.logisticsservice.service.LogisticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class LogisticsServiceImpl implements LogisticsService {

    private final LogisticsRepository logisticsRepository;
    private final LogisticsMapper logisticsMapper;
    private final OrderServiceClient orderServiceClient;

    @Override
    @Transactional
    public LogisticsResponse schedulePickup(CreateLogisticsRequest request) {
        log.info("Scheduling pickup for order: {}", request.orderId());

        if (logisticsRepository.existsByOrderId(request.orderId())) {
            throw new LogisticsAlreadyExistsException("Logistics already scheduled for order ID: " + request.orderId());
        }

        // Validate Order
        ApiResponse<OrderServiceResponse> orderResp = orderServiceClient.getOrderById(request.orderId());
        if (!orderResp.success() || orderResp.data() == null) {
            throw new ResourceNotFoundException("Order not found with ID: " + request.orderId());
        }
        if (!"CREATED".equalsIgnoreCase(orderResp.data().orderStatus())) {
            throw new BusinessValidationException("Logistics can only be scheduled for orders in CREATED status. Current: " + orderResp.data().orderStatus());
        }

        Logistics logistics = Logistics.builder()
                .orderId(request.orderId())
                .pickupDate(request.pickupDate())
                .trackingStatus(TrackingStatus.PICKUP_SCHEDULED)
                .remarks(request.remarks())
                .build();

        Logistics savedLogistics = logisticsRepository.save(logistics);

        // Update Order Status
        updateExternalOrderStatus(request.orderId(), "PICKUP_SCHEDULED", "Pickup scheduled for " + request.pickupDate());

        return logisticsMapper.toResponse(savedLogistics);
    }

    @Override
    @Transactional
    public LogisticsResponse assignVehicle(Long id, AssignVehicleRequest request) {
        Logistics logistics = logisticsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Logistics record not found with ID: " + id));

        if (logistics.getTrackingStatus() != TrackingStatus.PICKUP_SCHEDULED) {
            throw new InvalidTrackingStateException("Vehicle can only be assigned when status is PICKUP_SCHEDULED.");
        }

        logistics.setDriverName(request.driverName());
        logistics.setDriverPhone(request.driverPhone());
        logistics.setVehicleNumber(request.vehicleNumber());
        logistics.setVehicleType(request.vehicleType());
        logistics.setVehicleAssignedDate(LocalDateTime.now());
        logistics.setTrackingStatus(TrackingStatus.VEHICLE_ASSIGNED);

        Logistics updated = logisticsRepository.save(logistics);
        updateExternalOrderStatus(logistics.getOrderId(), "VEHICLE_ASSIGNED", "Vehicle " + request.vehicleNumber() + " assigned.");

        return logisticsMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public LogisticsResponse markVehicleArrived(Long id) {
        Logistics logistics = logisticsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Logistics record not found with ID: " + id));

        if (logistics.getTrackingStatus() != TrackingStatus.VEHICLE_ASSIGNED) {
            throw new InvalidTrackingStateException("Vehicle can only arrive after being assigned.");
        }

        logistics.setArrivalDate(LocalDateTime.now());
        logistics.setTrackingStatus(TrackingStatus.ARRIVED_AT_FARM);
        
        // Rule 10: Automatically move to PAYMENT_PENDING
        logistics.setTrackingStatus(TrackingStatus.PAYMENT_PENDING);

        Logistics updated = logisticsRepository.save(logistics);
        updateExternalOrderStatus(logistics.getOrderId(), "PAYMENT_PENDING", "Vehicle arrived at farm. Awaiting payment.");

        return logisticsMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public LogisticsResponse updateTrackingStatus(Long id, UpdateTrackingStatusRequest request) {
        Logistics logistics = logisticsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Logistics record not found with ID: " + id));

        validateStatusTransition(logistics.getTrackingStatus(), request.trackingStatus());

        logistics.setTrackingStatus(request.trackingStatus());
        if (request.remarks() != null) {
            logistics.setRemarks(request.remarks());
        }

        if (request.trackingStatus() == TrackingStatus.SHIPPED) {
            logistics.setShipmentDate(LocalDateTime.now());
            updateExternalOrderStatus(logistics.getOrderId(), "SHIPPED", request.remarks());
        } else if (request.trackingStatus() == TrackingStatus.DELIVERED) {
            logistics.setDeliveryDate(LocalDateTime.now());
            updateExternalOrderStatus(logistics.getOrderId(), "DELIVERED", request.remarks());
        }

        return logisticsMapper.toResponse(logisticsRepository.save(logistics));
    }

    @Override
    public LogisticsTrackingResponse trackShipment(Long orderId) {
        Logistics logistics = logisticsRepository.findByOrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Logistics record not found for order ID: " + orderId));
        return logisticsMapper.toTracking(logistics);
    }

    @Override
    public LogisticsResponse getLogisticsByOrderId(Long orderId) {
        Logistics logistics = logisticsRepository.findByOrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Logistics record not found for order ID: " + orderId));
        return logisticsMapper.toResponse(logistics);
    }

    @Override
    public LogisticsResponse getLogisticsById(Long id) {
        Logistics logistics = logisticsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Logistics record not found with ID: " + id));
        return logisticsMapper.toResponse(logistics);
    }

    @Override
    public List<LogisticsResponse> getAllLogistics() {
        return logisticsMapper.toResponseList(logisticsRepository.findAll());
    }

    @Override
    @Transactional
    public LogisticsResponse cancelLogistics(Long id) {
        Logistics logistics = logisticsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Logistics record not found with ID: " + id));

        if (logistics.getTrackingStatus() == TrackingStatus.SHIPPED || logistics.getTrackingStatus() == TrackingStatus.DELIVERED) {
            throw new InvalidTrackingStateException("Cannot cancel logistics once shipped or delivered.");
        }

        logistics.setTrackingStatus(TrackingStatus.CANCELLED);
        return logisticsMapper.toResponse(logisticsRepository.save(logistics));
    }

    private void validateStatusTransition(TrackingStatus current, TrackingStatus next) {
        if (current == TrackingStatus.DELIVERED || current == TrackingStatus.CANCELLED) {
            throw new InvalidTrackingStateException("Cannot transition from " + current);
        }

        // Rule 8: Cannot mark SHIPPED before LOADED
        if (next == TrackingStatus.SHIPPED && current != TrackingStatus.LOADED) {
            throw new InvalidTrackingStateException("Shipment requires status to be LOADED first.");
        }

        // Rule 9: Cannot mark DELIVERED before SHIPPED (or IN_TRANSIT)
        if (next == TrackingStatus.DELIVERED && (current != TrackingStatus.SHIPPED && current != TrackingStatus.IN_TRANSIT)) {
            throw new InvalidTrackingStateException("Delivery requires status to be SHIPPED or IN_TRANSIT first.");
        }
    }

    private void updateExternalOrderStatus(Long orderId, String status, String remarks) {
        try {
            orderServiceClient.updateOrderStatus(orderId, new OrderStatusUpdateRequest(status, remarks));
        } catch (Exception e) {
            log.error("Failed to update Order Service status: {}", e.getMessage());
        }
    }
}

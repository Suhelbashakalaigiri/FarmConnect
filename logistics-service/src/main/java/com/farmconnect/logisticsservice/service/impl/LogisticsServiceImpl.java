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
        updateExternalOrderStatus(logistics.getOrderId(), "CANCELLED", "Logistics cancelled.");
        return logisticsMapper.toResponse(logisticsRepository.save(logistics));
    }

    @Override
    @Transactional
    public LogisticsResponse startLoading(Long id) {
        return updateTracking(id, TrackingStatus.LOADING_STARTED, "Loading started at farm.");
    }

    @Override
    @Transactional
    public LogisticsResponse markLoaded(Long id) {
        return updateTracking(id, TrackingStatus.LOADED, "Vehicle loaded successfully.");
    }

    @Override
    @Transactional
    public LogisticsResponse markShipped(Long id) {
        Logistics logistics = logisticsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Logistics record not found with ID: " + id));
        
        validateStatusTransition(logistics.getTrackingStatus(), TrackingStatus.SHIPPED);
        
        logistics.setTrackingStatus(TrackingStatus.SHIPPED);
        logistics.setShipmentDate(LocalDateTime.now());
        logistics.setRemarks("Shipment dispatched from farm.");
        
        Logistics saved = logisticsRepository.save(logistics);
        updateExternalOrderStatus(saved.getOrderId(), "SHIPPED", saved.getRemarks());
        
        return logisticsMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public LogisticsResponse markInTransit(Long id) {
        return updateTracking(id, TrackingStatus.IN_TRANSIT, "Shipment is in transit.");
    }

    @Override
    @Transactional
    public LogisticsResponse markDelivered(Long id) {
        Logistics logistics = logisticsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Logistics record not found with ID: " + id));
        
        validateStatusTransition(logistics.getTrackingStatus(), TrackingStatus.DELIVERED);
        
        logistics.setTrackingStatus(TrackingStatus.DELIVERED);
        logistics.setDeliveryDate(LocalDateTime.now());
        logistics.setRemarks("Shipment delivered to buyer.");
        
        Logistics saved = logisticsRepository.save(logistics);
        updateExternalOrderStatus(saved.getOrderId(), "DELIVERED", saved.getRemarks());
        
        return logisticsMapper.toResponse(saved);
    }

    private LogisticsResponse updateTracking(Long id, TrackingStatus nextStatus, String remarks) {
        Logistics logistics = logisticsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Logistics record not found with ID: " + id));

        validateStatusTransition(logistics.getTrackingStatus(), nextStatus);

        logistics.setTrackingStatus(nextStatus);
        logistics.setRemarks(remarks);

        Logistics saved = logisticsRepository.save(logistics);
        updateExternalOrderStatus(saved.getOrderId(), mapToOrderStatus(nextStatus), remarks);

        return logisticsMapper.toResponse(saved);
    }

    private void validateStatusTransition(TrackingStatus current, TrackingStatus next) {
        if (current == TrackingStatus.DELIVERED || current == TrackingStatus.CANCELLED) {
            throw new InvalidTrackingTransitionException("Cannot transition from final status: " + current);
        }

        boolean isValid = switch (current) {
            case PICKUP_SCHEDULED -> next == TrackingStatus.VEHICLE_ASSIGNED || next == TrackingStatus.CANCELLED;
            case VEHICLE_ASSIGNED -> next == TrackingStatus.ARRIVED_AT_FARM || next == TrackingStatus.CANCELLED;
            case ARRIVED_AT_FARM -> next == TrackingStatus.PAYMENT_PENDING;
            case PAYMENT_PENDING -> next == TrackingStatus.PAYMENT_COMPLETED || next == TrackingStatus.CANCELLED;
            case PAYMENT_COMPLETED -> next == TrackingStatus.LOADING_STARTED;
            case LOADING_STARTED -> next == TrackingStatus.LOADED;
            case LOADED -> next == TrackingStatus.SHIPPED;
            case SHIPPED -> next == TrackingStatus.IN_TRANSIT;
            case IN_TRANSIT -> next == TrackingStatus.DELIVERED;
            default -> false;
        };

        if (!isValid) {
            throw new InvalidTrackingTransitionException("Invalid status transition: " + current + " -> " + next);
        }
    }

    private String mapToOrderStatus(TrackingStatus status) {
        return switch (status) {
            case PAYMENT_COMPLETED -> "PAYMENT_COMPLETED";
            case LOADING_STARTED -> "LOADING_PENDING";
            case LOADED -> "LOADED";
            case SHIPPED, IN_TRANSIT -> "SHIPPED";
            case DELIVERED -> "DELIVERED";
            case CANCELLED -> "CANCELLED";
            default -> status.name();
        };
    }

    private void updateExternalOrderStatus(Long orderId, String status, String remarks) {
        try {
            orderServiceClient.updateOrderStatus(orderId, new OrderStatusUpdateRequest(status, remarks));
        } catch (Exception e) {
            log.error("Failed to update Order Service status for order {}: {}", orderId, e.getMessage());
        }
    }
}

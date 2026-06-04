package com.farmconnect.logisticsservice.service;

import com.farmconnect.logisticsservice.dto.*;

import java.util.List;

public interface LogisticsService {
    LogisticsResponse schedulePickup(CreateLogisticsRequest request);
    LogisticsResponse assignVehicle(Long id, AssignVehicleRequest request);
    LogisticsResponse markVehicleArrived(Long id);
    LogisticsResponse updateTrackingStatus(Long id, UpdateTrackingStatusRequest request);
    LogisticsTrackingResponse trackShipment(Long orderId);
    LogisticsResponse getLogisticsByOrderId(Long orderId);
    LogisticsResponse getLogisticsById(Long id);
    List<LogisticsResponse> getAllLogistics();
    LogisticsResponse cancelLogistics(Long id);

    LogisticsResponse startLoading(Long id);

    LogisticsResponse markLoaded(Long id);

    LogisticsResponse markShipped(Long id);

    LogisticsResponse markInTransit(Long id);

    LogisticsResponse markDelivered(Long id);
}

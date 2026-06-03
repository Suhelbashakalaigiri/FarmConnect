package com.farmconnect.logisticsservice.repository;

import com.farmconnect.logisticsservice.entity.Logistics;
import com.farmconnect.logisticsservice.enums.TrackingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LogisticsRepository extends JpaRepository<Logistics, Long> {
    Optional<Logistics> findByOrderId(Long orderId);
    boolean existsByOrderId(Long orderId);
    List<Logistics> findByTrackingStatus(TrackingStatus status);
    Optional<Logistics> findByVehicleNumber(String vehicleNumber);
}

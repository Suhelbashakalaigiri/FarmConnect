package com.farmconnect.logisticsservice.entity;

import com.farmconnect.logisticsservice.enums.TrackingStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "logistics")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Logistics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long orderId;

    @Column(nullable = false)
    private LocalDateTime pickupDate;

    private LocalDateTime vehicleAssignedDate;

    private LocalDateTime arrivalDate;

    private LocalDateTime shipmentDate;

    private LocalDateTime deliveryDate;

    private String driverName;

    private String driverPhone;

    private String vehicleNumber;

    private String vehicleType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TrackingStatus trackingStatus;

    private String remarks;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}

package com.farmconnect.visitservice.entity;

import com.farmconnect.visitservice.enums.InspectionStatus;
import com.farmconnect.visitservice.enums.VisitStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "visits")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Visit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long bidId;

    @Column(nullable = false)
    private Long cropId;

    @Column(nullable = false)
    private Long buyerId;

    @Column(nullable = false)
    private Long farmerId;

    @Column(nullable = false)
    private LocalDateTime visitDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VisitStatus visitStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InspectionStatus inspectionStatus;

    private String buyerRemarks;

    private String farmerRemarks;

    private LocalDateTime approvedAt;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}

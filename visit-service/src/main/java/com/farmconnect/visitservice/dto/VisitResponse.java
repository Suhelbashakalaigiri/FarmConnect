package com.farmconnect.visitservice.dto;

import com.farmconnect.visitservice.enums.InspectionStatus;
import com.farmconnect.visitservice.enums.VisitStatus;
import java.time.LocalDateTime;

public record VisitResponse(
    Long id,
    Long bidId,
    Long cropId,
    Long buyerId,
    Long farmerId,
    LocalDateTime visitDate,
    VisitStatus visitStatus,
    InspectionStatus inspectionStatus,
    String buyerRemarks,
    String farmerRemarks,
    LocalDateTime approvedAt,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}

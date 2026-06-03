package com.farmconnect.visitservice.dto;

import com.farmconnect.visitservice.enums.InspectionStatus;
import com.farmconnect.visitservice.enums.VisitStatus;
import java.time.LocalDateTime;

public record VisitSummaryResponse(
    Long id,
    Long bidId,
    Long cropId,
    LocalDateTime visitDate,
    VisitStatus visitStatus,
    InspectionStatus inspectionStatus
) {}

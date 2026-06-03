package com.farmconnect.visitservice.service;

import com.farmconnect.visitservice.dto.*;

import java.util.List;

public interface VisitService {
    VisitResponse scheduleVisit(CreateVisitRequest request);
    VisitResponse getVisitById(Long id);
    List<VisitResponse> getVisitsByBuyer(Long buyerId);
    List<VisitResponse> getVisitsByFarmer(Long farmerId);
    VisitResponse getVisitByBid(Long bidId);
    VisitResponse rescheduleVisit(Long id, RescheduleVisitRequest request);
    VisitResponse markVisited(Long id);
    VisitResponse approveInspection(Long id, InspectionApprovalRequest request);
    VisitResponse rejectInspection(Long id, InspectionRejectionRequest request);
    VisitResponse cancelVisit(Long id);
}

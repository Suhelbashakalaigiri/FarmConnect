package com.farmconnect.visitservice.controller;

import com.farmconnect.visitservice.dto.*;
import com.farmconnect.visitservice.service.VisitService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/visits")
@RequiredArgsConstructor
public class VisitController {

    private final VisitService visitService;

    @PostMapping
    public ResponseEntity<ApiResponse<VisitResponse>> scheduleVisit(
            @RequestBody(required = false) @Valid CreateVisitRequest request,
            @RequestParam(required = false) Long bidId,
            @RequestParam(required = false) Long farmerId,
            @RequestParam(required = false) Long buyerId,
            @RequestParam(required = false) Long cropId) {
        
        VisitResponse response;
        if (request != null) {
            response = visitService.scheduleVisit(request);
        } else {
            // Handle automated call from Bidding Service
            CreateVisitRequest automatedRequest = new CreateVisitRequest(bidId, farmerId, buyerId, cropId, java.time.LocalDateTime.now().plusDays(2));
            response = visitService.scheduleVisit(automatedRequest);
        }
        return new ResponseEntity<>(ApiResponse.success("Visit scheduled successfully", HttpStatus.CREATED.value(), response), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<VisitResponse>>> getAllVisits() {
        // Implementation for listing could be added to service if needed
        return ResponseEntity.ok(ApiResponse.success("Visit listing not fully implemented for all", HttpStatus.OK.value(), null));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<VisitResponse>> getVisitById(@PathVariable Long id) {
        VisitResponse response = visitService.getVisitById(id);
        return ResponseEntity.ok(ApiResponse.success("Visit fetched successfully", HttpStatus.OK.value(), response));
    }

    @GetMapping("/buyer/{buyerId}")
    public ResponseEntity<ApiResponse<List<VisitResponse>>> getVisitsByBuyer(@PathVariable Long buyerId) {
        List<VisitResponse> response = visitService.getVisitsByBuyer(buyerId);
        return ResponseEntity.ok(ApiResponse.success("Buyer visits fetched successfully", HttpStatus.OK.value(), response));
    }

    @GetMapping("/farmer/{farmerId}")
    public ResponseEntity<ApiResponse<List<VisitResponse>>> getVisitsByFarmer(@PathVariable Long farmerId) {
        List<VisitResponse> response = visitService.getVisitsByFarmer(farmerId);
        return ResponseEntity.ok(ApiResponse.success("Farmer visits fetched successfully", HttpStatus.OK.value(), response));
    }

    @GetMapping("/bid/{bidId}")
    public ResponseEntity<ApiResponse<VisitResponse>> getVisitByBid(@PathVariable Long bidId) {
        VisitResponse response = visitService.getVisitByBid(bidId);
        return ResponseEntity.ok(ApiResponse.success("Visit for bid fetched successfully", HttpStatus.OK.value(), response));
    }

    @PutMapping("/{id}/reschedule")
    public ResponseEntity<ApiResponse<VisitResponse>> rescheduleVisit(@PathVariable Long id, @Valid @RequestBody RescheduleVisitRequest request) {
        VisitResponse response = visitService.rescheduleVisit(id, request);
        return ResponseEntity.ok(ApiResponse.success("Visit rescheduled successfully", HttpStatus.OK.value(), response));
    }

    @PutMapping("/{id}/mark-visited")
    public ResponseEntity<ApiResponse<VisitResponse>> markVisited(@PathVariable Long id) {
        VisitResponse response = visitService.markVisited(id);
        return ResponseEntity.ok(ApiResponse.success("Visit marked as visited", HttpStatus.OK.value(), response));
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<ApiResponse<VisitResponse>> approveInspection(
            @PathVariable Long id, 
            @RequestBody(required = false) InspectionApprovalRequest request) {
        if (request == null) {
            request = new InspectionApprovalRequest("Approved via system request");
        }
        VisitResponse response = visitService.approveInspection(id, request);
        return ResponseEntity.ok(ApiResponse.success("Inspection approved successfully", HttpStatus.OK.value(), response));
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<ApiResponse<VisitResponse>> rejectInspection(
            @PathVariable Long id, 
            @RequestBody(required = false) InspectionRejectionRequest request) {
        if (request == null) {
            request = new InspectionRejectionRequest("Rejected via system request");
        }
        VisitResponse response = visitService.rejectInspection(id, request);
        return ResponseEntity.ok(ApiResponse.success("Inspection rejected successfully", HttpStatus.OK.value(), response));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<VisitResponse>> cancelVisit(@PathVariable Long id) {
        VisitResponse response = visitService.cancelVisit(id);
        return ResponseEntity.ok(ApiResponse.success("Visit cancelled successfully", HttpStatus.OK.value(), response));
    }
}

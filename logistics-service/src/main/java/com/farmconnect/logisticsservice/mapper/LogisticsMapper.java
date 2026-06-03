package com.farmconnect.logisticsservice.mapper;

import com.farmconnect.logisticsservice.dto.LogisticsResponse;
import com.farmconnect.logisticsservice.dto.LogisticsSummaryResponse;
import com.farmconnect.logisticsservice.dto.LogisticsTrackingResponse;
import com.farmconnect.logisticsservice.entity.Logistics;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LogisticsMapper {
    LogisticsResponse toResponse(Logistics logistics);
    LogisticsTrackingResponse toTracking(Logistics logistics);
    LogisticsSummaryResponse toSummary(Logistics logistics);
    List<LogisticsResponse> toResponseList(List<Logistics> logisticsList);
}

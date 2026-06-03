package com.farmconnect.visitservice.mapper;

import com.farmconnect.visitservice.dto.VisitResponse;
import com.farmconnect.visitservice.dto.VisitSummaryResponse;
import com.farmconnect.visitservice.entity.Visit;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface VisitMapper {
    VisitResponse toResponse(Visit visit);
    VisitSummaryResponse toSummary(Visit visit);
    List<VisitResponse> toResponseList(List<Visit> visits);
}

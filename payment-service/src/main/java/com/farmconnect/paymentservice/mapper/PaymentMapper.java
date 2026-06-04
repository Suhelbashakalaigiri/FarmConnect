package com.farmconnect.paymentservice.mapper;

import com.farmconnect.paymentservice.dto.PaymentResponse;
import com.farmconnect.paymentservice.dto.PaymentSummaryResponse;
import com.farmconnect.paymentservice.dto.TransactionHistoryResponse;
import com.farmconnect.paymentservice.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PaymentMapper {
    PaymentResponse toResponse(Payment payment);
    PaymentSummaryResponse toSummary(Payment payment);
    TransactionHistoryResponse toHistory(Payment payment);
    List<PaymentResponse> toResponseList(List<Payment> payments);
}

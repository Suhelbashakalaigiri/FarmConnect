package com.farmconnect.paymentservice.service;

import com.farmconnect.paymentservice.dto.*;

import java.util.List;

public interface PaymentService {
    PaymentResponse initiatePayment(CreatePaymentRequest request);
    PaymentResponse verifyPayment(PaymentVerificationRequest request);
    PaymentResponse getPaymentById(Long id);
    List<PaymentResponse> getPaymentsByOrder(Long orderId);
    List<PaymentResponse> getPaymentsByBuyer(Long buyerId);
    List<TransactionHistoryResponse> getTransactionHistory(Long buyerId);
}

package com.farmconnect.paymentservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PaymentVerificationRequest(
    @NotNull(message = "Local Payment ID is required")
    Long paymentId,
    
    @NotBlank(message = "Razorpay Order ID is required")
    String razorpayOrderId,
    
    @NotBlank(message = "Razorpay Payment ID is required")
    String razorpayPaymentId,
    
    @NotBlank(message = "Razorpay Signature is required")
    String razorpaySignature
) {}

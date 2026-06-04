package com.farmconnect.paymentservice.service.impl;

import com.farmconnect.paymentservice.dto.*;
import com.farmconnect.paymentservice.entity.Payment;
import com.farmconnect.paymentservice.enums.PaymentStatus;
import com.farmconnect.paymentservice.exception.*;
import com.farmconnect.paymentservice.feign.client.LogisticsServiceClient;
import com.farmconnect.paymentservice.feign.client.OrderServiceClient;
import com.farmconnect.paymentservice.feign.dto.LogisticsServiceResponse;
import com.farmconnect.paymentservice.feign.dto.LogisticsStatusUpdateRequest;
import com.farmconnect.paymentservice.feign.dto.OrderServiceResponse;
import com.farmconnect.paymentservice.mapper.PaymentMapper;
import com.farmconnect.paymentservice.repository.PaymentRepository;
import com.farmconnect.paymentservice.service.PaymentService;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final RazorpayClient razorpayClient;
    private final OrderServiceClient orderServiceClient;
    private final LogisticsServiceClient logisticsServiceClient;

    @Value("${razorpay.key-secret}")
    private String razorpaySecret;

    @Override
    @Transactional
    public PaymentResponse initiatePayment(CreatePaymentRequest request) {
        log.info("Initiating payment for order ID: {}", request.orderId());

        // 1. Rule 2: Duplicate successful payment not allowed
        if (paymentRepository.existsByOrderIdAndPaymentStatus(request.orderId(), PaymentStatus.SUCCESS)) {
            throw new PaymentAlreadyCompletedException("Payment already completed for order ID: " + request.orderId());
        }

        // 2. Rule 5: Validate Order
        ApiResponse<OrderServiceResponse> orderResp = orderServiceClient.getOrderById(request.orderId());
        if (!orderResp.success() || orderResp.data() == null) {
            throw new ResourceNotFoundException("Order not found with ID: " + request.orderId());
        }
        if ("CANCELLED".equalsIgnoreCase(orderResp.data().orderStatus())) {
            throw new InvalidPaymentException("Cannot pay for a CANCELLED order.");
        }

        // 3. Rule 3: Amount Validation
        if (orderResp.data().totalAmount().compareTo(request.amount()) != 0) {
            throw new InvalidPaymentException("Payment amount does not match order amount.");
        }

        // 4. Rule 4: Logistics Status Validation
        ApiResponse<LogisticsServiceResponse> logisticsResp = logisticsServiceClient.getLogisticsByOrderId(request.orderId());
        if (!logisticsResp.success() || logisticsResp.data() == null) {
            throw new InvalidPaymentException("Logistics record not found for order ID: " + request.orderId());
        }
        if (!"PAYMENT_PENDING".equalsIgnoreCase(logisticsResp.data().trackingStatus())) {
            throw new InvalidPaymentException("Payment can only be initiated when logistics status is PAYMENT_PENDING. Current: " + logisticsResp.data().trackingStatus());
        }

        try {
            // Razorpay Order Creation
            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", request.amount().multiply(new java.math.BigDecimal(100)).intValue()); // Paisa
            orderRequest.put("currency", request.currency());
            orderRequest.put("receipt", "receipt_" + request.orderId());

            Order razorpayOrder = razorpayClient.orders.create(orderRequest);

            Payment payment = Payment.builder()
                    .orderId(request.orderId())
                    .buyerId(request.buyerId())
                    .farmerId(request.farmerId())
                    .amount(request.amount())
                    .currency(request.currency())
                    .razorpayOrderId(razorpayOrder.get("id"))
                    .paymentStatus(PaymentStatus.PENDING)
                    .transactionReference("TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                    .build();

            return paymentMapper.toResponse(paymentRepository.save(payment));
        } catch (Exception e) {
            log.error("Razorpay order creation failed: {}", e.getMessage());
            throw new PaymentGatewayException("Failed to initiate payment with gateway: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public PaymentResponse verifyPayment(PaymentVerificationRequest request) {
        log.info("Verifying payment for Razorpay Order ID: {}", request.razorpayOrderId());

        Payment payment = paymentRepository.findById(request.paymentId())
                .orElseThrow(() -> new ResourceNotFoundException("Payment record not found with ID: " + request.paymentId()));

        if (payment.getPaymentStatus() == PaymentStatus.SUCCESS) {
            return paymentMapper.toResponse(payment);
        }

        // 5. Rule 7: Signature verification mandatory
        try {
            JSONObject options = new JSONObject();
            options.put("razorpay_order_id", request.razorpayOrderId());
            options.put("razorpay_payment_id", request.razorpayPaymentId());
            options.put("razorpay_signature", request.razorpaySignature());

            boolean isValid = Utils.verifyPaymentSignature(options, razorpaySecret);

            if (!isValid) {
                payment.setPaymentStatus(PaymentStatus.FAILED);
                paymentRepository.save(payment);
                throw new PaymentVerificationException("Invalid payment signature.");
            }

            // Update Payment Record
            payment.setRazorpayPaymentId(request.razorpayPaymentId());
            payment.setRazorpaySignature(request.razorpaySignature());
            payment.setPaymentStatus(PaymentStatus.SUCCESS);
            payment.setPaymentDate(LocalDateTime.now());
            payment.setPaymentMethod("RAZORPAY"); // Detailed method could be fetched via API if needed

            Payment savedPayment = paymentRepository.save(payment);

            // 6. Update Logistics Status
            updateLogisticsStatus(payment.getOrderId());

            return paymentMapper.toResponse(savedPayment);
        } catch (Exception e) {
            log.error("Payment verification failed: {}", e.getMessage());
            throw new PaymentVerificationException("Payment verification failed: " + e.getMessage());
        }
    }

    @Override
    public PaymentResponse getPaymentById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with ID: " + id));
        return paymentMapper.toResponse(payment);
    }

    @Override
    public List<PaymentResponse> getPaymentsByOrder(Long orderId) {
        return paymentMapper.toResponseList(paymentRepository.findByOrderId(orderId));
    }

    @Override
    public List<PaymentResponse> getPaymentsByBuyer(Long buyerId) {
        return paymentMapper.toResponseList(paymentRepository.findByBuyerId(buyerId));
    }

    @Override
    public List<TransactionHistoryResponse> getTransactionHistory(Long buyerId) {
        return paymentRepository.findByBuyerId(buyerId).stream()
                .map(paymentMapper::toHistory)
                .toList();
    }

    private void updateLogisticsStatus(Long orderId) {
        try {
            ApiResponse<LogisticsServiceResponse> logisticsResp = logisticsServiceClient.getLogisticsByOrderId(orderId);
            if (logisticsResp.success() && logisticsResp.data() != null) {
                logisticsServiceClient.updateTrackingStatus(
                        logisticsResp.data().id(),
                        new LogisticsStatusUpdateRequest("PAYMENT_COMPLETED", "Payment verified via Razorpay.")
                );
            }
        } catch (Exception e) {
            log.error("Failed to update logistics status after payment: {}", e.getMessage());
        }
    }
}

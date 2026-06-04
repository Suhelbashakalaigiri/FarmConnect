package com.farmconnect.paymentservice.controller;

import com.farmconnect.paymentservice.dto.*;
import com.farmconnect.paymentservice.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/initiate")
    public ResponseEntity<ApiResponse<PaymentResponse>> initiatePayment(@Valid @RequestBody CreatePaymentRequest request) {
        PaymentResponse response = paymentService.initiatePayment(request);
        return new ResponseEntity<>(ApiResponse.success("Payment initiated successfully", HttpStatus.CREATED.value(), response), HttpStatus.CREATED);
    }

    @PostMapping("/verify")
    public ResponseEntity<ApiResponse<PaymentResponse>> verifyPayment(@Valid @RequestBody PaymentVerificationRequest request) {
        PaymentResponse response = paymentService.verifyPayment(request);
        return ResponseEntity.ok(ApiResponse.success("Payment verified successfully", HttpStatus.OK.value(), response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PaymentResponse>> getPaymentById(@PathVariable Long id) {
        PaymentResponse response = paymentService.getPaymentById(id);
        return ResponseEntity.ok(ApiResponse.success("Payment fetched successfully", HttpStatus.OK.value(), response));
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<ApiResponse<List<PaymentResponse>>> getPaymentsByOrder(@PathVariable Long orderId) {
        List<PaymentResponse> response = paymentService.getPaymentsByOrder(orderId);
        return ResponseEntity.ok(ApiResponse.success("Order payments fetched successfully", HttpStatus.OK.value(), response));
    }

    @GetMapping("/history/{buyerId}")
    public ResponseEntity<ApiResponse<List<TransactionHistoryResponse>>> getTransactionHistory(@PathVariable Long buyerId) {
        List<TransactionHistoryResponse> response = paymentService.getTransactionHistory(buyerId);
        return ResponseEntity.ok(ApiResponse.success("Transaction history fetched successfully", HttpStatus.OK.value(), response));
    }
}

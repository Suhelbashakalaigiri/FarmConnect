package com.farmconnect.paymentservice.repository;

import com.farmconnect.paymentservice.entity.Payment;
import com.farmconnect.paymentservice.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByOrderId(Long orderId);
    List<Payment> findByBuyerId(Long buyerId);
    Optional<Payment> findByRazorpayOrderId(String razorpayOrderId);
    Optional<Payment> findByTransactionReference(String reference);
    boolean existsByOrderIdAndPaymentStatus(Long orderId, PaymentStatus status);
}

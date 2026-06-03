package com.farmconnect.orderservice.repository;

import com.farmconnect.orderservice.entity.Order;
import com.farmconnect.orderservice.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByBuyerId(Long buyerId);
    List<Order> findByFarmerId(Long farmerId);
    Optional<Order> findByVisitId(Long visitId);
    Optional<Order> findByBidId(Long bidId);
    boolean existsByVisitId(Long visitId);
    boolean existsByBidId(Long bidId);
    List<Order> findByOrderStatus(OrderStatus status);
    Optional<Order> findByOrderNumber(String orderNumber);
}

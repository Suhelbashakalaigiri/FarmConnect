package com.farmconnect.visitservice.repository;

import com.farmconnect.visitservice.entity.Visit;
import com.farmconnect.visitservice.enums.InspectionStatus;
import com.farmconnect.visitservice.enums.VisitStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VisitRepository extends JpaRepository<Visit, Long> {
    Optional<Visit> findByBidId(Long bidId);
    List<Visit> findByBuyerId(Long buyerId);
    List<Visit> findByFarmerId(Long farmerId);
    boolean existsByBidId(Long bidId);
    List<Visit> findByVisitStatus(VisitStatus status);
    List<Visit> findByInspectionStatus(InspectionStatus status);
}

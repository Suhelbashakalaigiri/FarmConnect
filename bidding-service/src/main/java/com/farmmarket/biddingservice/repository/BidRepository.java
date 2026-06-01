package com.farmmarket.biddingservice.repository;

import com.farmmarket.biddingservice.entity.Bid;
import com.farmmarket.biddingservice.enums.BidStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BidRepository extends JpaRepository<Bid, Long> {

    List<Bid> findByCropId(Long cropId);

    List<Bid> findByBuyerId(Long buyerId);

    @Query("SELECT b FROM Bid b WHERE b.cropId = :cropId AND b.bidStatus = 'ACTIVE' ORDER BY b.bidAmount DESC LIMIT 1")
    Optional<Bid> findHighestBidByCropId(@Param("cropId") Long cropId);

    boolean existsByCropIdAndBidStatus(Long cropId, BidStatus bidStatus);

    List<Bid> findAllByCropId(Long cropId);
    
    List<Bid> findByCropIdAndBidStatus(Long cropId, BidStatus bidStatus);
}

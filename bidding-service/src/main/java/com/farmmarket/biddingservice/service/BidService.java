package com.farmmarket.biddingservice.service;

import com.farmmarket.biddingservice.dto.*;
import java.util.List;

public interface BidService {
    BidResponse createBid(CreateBidRequest request);
    BidResponse getBidById(Long id);
    List<BidResponse> getAllBids();
    List<BidResponse> getBidsByCrop(Long cropId);
    List<BidResponse> getBidsByBuyer(Long buyerId);
    HighestBidResponse getHighestBid(Long cropId);
    BidResponse acceptBid(Long id);
    BidResponse rejectBid(Long id);
    BidResponse withdrawBid(Long id);
}

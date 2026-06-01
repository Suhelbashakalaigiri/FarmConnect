package com.farmmarket.biddingservice.mapper;

import com.farmmarket.biddingservice.dto.BidResponse;
import com.farmmarket.biddingservice.dto.BidSummaryResponse;
import com.farmmarket.biddingservice.dto.HighestBidResponse;
import com.farmmarket.biddingservice.entity.Bid;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BidMapper {
    BidResponse toResponse(Bid bid);
    BidSummaryResponse toSummaryResponse(Bid bid);
    HighestBidResponse toHighestBidResponse(Bid bid);
    List<BidResponse> toResponseList(List<Bid> bids);
}

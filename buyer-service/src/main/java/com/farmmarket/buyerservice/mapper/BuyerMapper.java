package com.farmmarket.buyerservice.mapper;

import com.farmmarket.buyerservice.dto.BuyerRequest;
import com.farmmarket.buyerservice.dto.BuyerResponse;
import com.farmmarket.buyerservice.entity.Buyer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BuyerMapper {

    Buyer toEntity(BuyerRequest request);

    BuyerResponse toResponse(Buyer buyer);

    @Mapping(target = "buyerId", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    void updateBuyerFromRequest(BuyerRequest request, @MappingTarget Buyer buyer);
}

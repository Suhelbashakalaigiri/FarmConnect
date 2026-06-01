package com.farmmarket.farmerservice.mapper;

import com.farmmarket.farmerservice.dto.CreateFarmerRequest;
import com.farmmarket.farmerservice.dto.FarmerResponse;
import com.farmmarket.farmerservice.dto.UpdateFarmerRequest;
import com.farmmarket.farmerservice.entity.Farmer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface FarmerMapper {
    FarmerMapper INSTANCE = Mappers.getMapper(FarmerMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "verificationStatus", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Farmer toEntity(CreateFarmerRequest request);

    FarmerResponse toResponse(Farmer farmer);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "phoneNumber", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "aadhaarNumber", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "verificationStatus", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromRequest(UpdateFarmerRequest request, @MappingTarget Farmer farmer);
}

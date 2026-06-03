package com.farmmarket.farmerservice.service;

import com.farmmarket.farmerservice.dto.*;
import com.farmmarket.farmerservice.enums.FarmerAvailabilityStatus;

import java.util.List;

public interface FarmerService {
    FarmerResponse createFarmer(CreateFarmerRequest request);
    FarmerResponse getFarmerById(Long id);
    List<FarmerResponse> getAllFarmers();
    FarmerResponse updateFarmer(Long id, UpdateFarmerRequest request);
    void deleteFarmer(Long id);
    List<CropSummary> getFarmerCropSummary(Long id);
    List<OrderSummary> getFarmerOrdersSummary(Long id);
    FarmerResponse updateAvailability(Long id, FarmerAvailabilityStatus status);
}

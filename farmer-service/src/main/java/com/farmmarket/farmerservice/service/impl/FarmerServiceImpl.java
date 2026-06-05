package com.farmmarket.farmerservice.service.impl;

import com.farmmarket.farmerservice.dto.*;
import com.farmmarket.farmerservice.entity.Farmer;
import com.farmmarket.farmerservice.enums.FarmerAvailabilityStatus;
import com.farmmarket.farmerservice.enums.FarmerStatus;
import com.farmmarket.farmerservice.enums.Status;
import com.farmmarket.farmerservice.enums.VerificationStatus;
import com.farmmarket.farmerservice.exception.InvalidOperationException;
import com.farmmarket.farmerservice.exception.ResourceAlreadyExistsException;
import com.farmmarket.farmerservice.exception.ResourceNotFoundException;
import com.farmmarket.farmerservice.mapper.FarmerMapper;
import com.farmmarket.farmerservice.repository.FarmerRepository;
import com.farmmarket.farmerservice.service.FarmerService;
import com.farmmarket.farmerservice.exception.IntegrationException;
import com.farmmarket.farmerservice.feign.CropServiceClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FarmerServiceImpl implements FarmerService {

    private final FarmerRepository farmerRepository;
    private final FarmerMapper farmerMapper;
    private final CropServiceClient cropServiceClient;

    @Override
    @Transactional
    public FarmerResponse createInternalProfile(CreateFarmerProfileRequest request) {
        log.info("Creating internal farmer profile for ID: {}", request.id());
        
        if (farmerRepository.existsById(request.id())) {
            throw new ResourceAlreadyExistsException("Farmer with ID " + request.id() + " already exists");
        }
        if (farmerRepository.existsByEmail(request.email())) {
            throw new ResourceAlreadyExistsException("Farmer with email " + request.email() + " already exists");
        }

        Farmer farmer = Farmer.builder()
                .id(request.id())
                .fullName(request.fullName())
                .email(request.email())
                .phoneNumber(request.phoneNumber())
                .status(FarmerStatus.ACTIVE)
                .verificationStatus(VerificationStatus.PENDING)
                .availabilityStatus(FarmerAvailabilityStatus.AVAILABLE)
                .profileCompleted(false)
                .build();

        Farmer savedFarmer = farmerRepository.save(farmer);
        return farmerMapper.toResponse(savedFarmer);
    }

    @Override
    @Transactional
    public FarmerResponse completeProfile(Long farmerId, FarmerProfileUpdateRequest request) {
        log.info("Completing farmer profile for ID: {}", farmerId);
        
        Farmer farmer = farmerRepository.findById(farmerId)
                .orElseThrow(() -> new ResourceNotFoundException("Farmer not found with ID: " + farmerId));

        farmer.setLandArea(request.landArea());
        farmer.setLandUnit(request.landUnit());
        farmer.setFarmingType(request.farmingType());
        farmer.setAddressLine(request.addressLine());
        farmer.setVillage(request.village());
        farmer.setMandal(request.mandal());
        farmer.setDistrict(request.district());
        farmer.setState(request.state());
        farmer.setPincode(request.pincode());
        farmer.setProfileCompleted(true);

        Farmer updatedFarmer = farmerRepository.save(farmer);
        return farmerMapper.toResponse(updatedFarmer);
    }

    @Override
    public FarmerResponse getFarmerById(Long id) {
        Farmer farmer = farmerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Farmer not found with ID: " + id));
        return farmerMapper.toResponse(farmer);
    }

    @Override
    public List<FarmerResponse> getAllFarmers() {
        return farmerRepository.findAll().stream()
                .map(farmerMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public FarmerResponse updateFarmer(Long id, UpdateFarmerRequest request) {
        Farmer farmer = farmerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Farmer not found with ID: " + id));

        // Blocked farmers cannot update profile
        if (farmer.getStatus() == FarmerStatus.BLOCKED) {
            throw new InvalidOperationException("Blocked farmers cannot update their profile");
        }

        // Age Validation for updated DOB (if provided)
        if (request.dateOfBirth() != null && Period.between(request.dateOfBirth(), LocalDate.now()).getYears() < 18) {
            throw new InvalidOperationException("Farmer must be at least 18 years old");
        }

        farmerMapper.updateEntityFromRequest(request, farmer);
        
        if (isProfileComplete(farmer)) {
            farmer.setProfileCompleted(true);
        }
        
        Farmer updatedFarmer = farmerRepository.save(farmer);
        return farmerMapper.toResponse(updatedFarmer);
    }

    private boolean isProfileComplete(Farmer farmer) {
        return farmer.getLandArea() != null &&
               farmer.getLandUnit() != null &&
               farmer.getFarmingType() != null &&
               farmer.getAddressLine() != null && !farmer.getAddressLine().isBlank() &&
               farmer.getVillage() != null && !farmer.getVillage().isBlank() &&
               farmer.getDistrict() != null && !farmer.getDistrict().isBlank() &&
               farmer.getState() != null && !farmer.getState().isBlank() &&
               farmer.getPincode() != null && !farmer.getPincode().isBlank();
    }

    @Override
    @Transactional
    public void deleteFarmer(Long id) {
        if (!farmerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Farmer not found with ID: " + id);
        }
        farmerRepository.deleteById(id);
    }

    @Override
    public List<CropSummary> getFarmerCropSummary(Long id) {
        // Verify farmer exists
        if (!farmerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Farmer not found with ID: " + id);
        }

        ApiResponse<List<CropSummary>> response = cropServiceClient.getCropsByFarmerId(id);
        if (response.success() && response.data() != null) {
            return response.data();
        }
        return List.of();
    }

    @Override
    public List<OrderSummary> getFarmerOrdersSummary(Long id) {
        throw new IntegrationException("Order Service not implemented yet.");
    }

    @Override
    @Transactional
    public FarmerResponse updateAvailability(Long id, FarmerAvailabilityStatus status) {
        Farmer farmer = farmerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Farmer not found with ID: " + id));
        farmer.setAvailabilityStatus(status);
        return farmerMapper.toResponse(farmerRepository.save(farmer));
    }
}

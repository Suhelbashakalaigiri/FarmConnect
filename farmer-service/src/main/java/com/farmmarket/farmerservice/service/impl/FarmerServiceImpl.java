package com.farmmarket.farmerservice.service.impl;

import com.farmmarket.farmerservice.dto.*;
import com.farmmarket.farmerservice.entity.Farmer;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FarmerServiceImpl implements FarmerService {

    private final FarmerRepository farmerRepository;
    private final FarmerMapper farmerMapper;
    private final CropServiceClient cropServiceClient;

    @Override
    @Transactional
    public FarmerResponse createFarmer(CreateFarmerRequest request) {
        // Business Validations
        if (farmerRepository.existsByEmail(request.email())) {
            throw new ResourceAlreadyExistsException("Email already registered: " + request.email());
        }
        if (farmerRepository.existsByPhoneNumber(request.phoneNumber())) {
            throw new ResourceAlreadyExistsException("Phone number already registered: " + request.phoneNumber());
        }
        if (farmerRepository.existsByAadhaarNumber(request.aadhaarNumber())) {
            throw new ResourceAlreadyExistsException("Aadhaar number already registered: " + request.aadhaarNumber());
        }

        // Age Validation (18+)
        if (Period.between(request.dateOfBirth(), LocalDate.now()).getYears() < 18) {
            throw new InvalidOperationException("Farmer must be at least 18 years old");
        }

        Farmer farmer = farmerMapper.toEntity(request);
        farmer.setStatus(FarmerStatus.ACTIVE);
        farmer.setVerificationStatus(VerificationStatus.PENDING);

        Farmer savedFarmer = farmerRepository.save(farmer);
        return farmerMapper.toResponse(savedFarmer);
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

        // Age Validation for updated DOB
        if (Period.between(request.dateOfBirth(), LocalDate.now()).getYears() < 18) {
            throw new InvalidOperationException("Farmer must be at least 18 years old");
        }

        farmerMapper.updateEntityFromRequest(request, farmer);
        Farmer updatedFarmer = farmerRepository.save(farmer);
        return farmerMapper.toResponse(updatedFarmer);
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
}

package com.farmconnect.crop.service.Impl;

import com.farmconnect.crop.dto.CreateCropRequest;
import com.farmconnect.crop.dto.CropResponse;
import com.farmconnect.crop.entity.Category;
import com.farmconnect.crop.entity.Crop;
import com.farmconnect.crop.exception.ResourceAlreadyExistsException;
import com.farmconnect.crop.exception.ResourceNotFoundException;
import com.farmconnect.crop.mapper.CropMapper;
import com.farmconnect.crop.repository.CategoryRepository;
import com.farmconnect.crop.repository.CropRepository;
import com.farmconnect.crop.service.CropService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import com.farmconnect.crop.dto.ApiResponse;
import com.farmconnect.crop.dto.CreateCropRequest;
import com.farmconnect.crop.dto.CropResponse;
import com.farmconnect.crop.dto.ExternalFarmerResponse;
import com.farmconnect.crop.entity.Category;
import com.farmconnect.crop.entity.Crop;
import com.farmconnect.crop.exception.BusinessValidationException;
import com.farmconnect.crop.exception.ResourceAlreadyExistsException;
import com.farmconnect.crop.exception.ResourceNotFoundException;
import com.farmconnect.crop.feign.FarmerServiceClient;
import com.farmconnect.crop.mapper.CropMapper;
import com.farmconnect.crop.repository.CategoryRepository;
import com.farmconnect.crop.repository.CropRepository;
import com.farmconnect.crop.service.CropService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
...
@Service
@RequiredArgsConstructor
@Slf4j
public class CropServiceImpl implements CropService {

    private static final String UPLOAD_DIR = "uploads/products/";
    private final CropMapper cropMapper;
    private final CropRepository cropRepository;
    private final CategoryRepository categoryRepository;
    private final FarmerServiceClient farmerServiceClient;

    @Transactional
    @Override
    public CropResponse addCrop(CreateCropRequest crop) {
        // 1. Validate Category
        Category category = categoryRepository.findByCategoryNameIgnoreCase(crop.categoryName())
                .orElseThrow(() -> new ResourceNotFoundException("Category Not Found: " + crop.categoryName()));

        // 2. Validate Farmer via Farmer Service
        log.info("Validating farmerId: {} via Farmer Service", crop.farmerId());
        ApiResponse<ExternalFarmerResponse> farmerResponse = farmerServiceClient.getFarmerById(crop.farmerId());

        if (!farmerResponse.success() || farmerResponse.data() == null) {
            throw new ResourceNotFoundException("Farmer not found with ID: " + crop.farmerId());
        }

        ExternalFarmerResponse farmer = farmerResponse.data();
        if (!"ACTIVE".equalsIgnoreCase(farmer.status())) {
            throw new BusinessValidationException("Farmer is not ACTIVE. Status: " + farmer.status());
        }
        if (!"VERIFIED".equalsIgnoreCase(farmer.verificationStatus())) {
            throw new BusinessValidationException("Farmer is not VERIFIED. Verification Status: " + farmer.verificationStatus());
        }

        if(cropRepository.existsByCropNameIgnoreCase(crop.cropName())){
            throw new ResourceAlreadyExistsException("Crop Already Exists with name: " + crop.cropName());
        }

        Crop newCrop = cropMapper.toEntity(crop);
        newCrop.setCategory(category);
        newCrop.setFarmerId(crop.farmerId());
        newCrop.setCreatedAt(LocalDateTime.now());

        return cropMapper.toDto(cropRepository.save(newCrop));
    }
...
    @Override
    public List<CropResponse> getAllCrops() {
        return cropRepository.findAll().stream().map(cropMapper::toDto).toList();
    }

    @Override
    public List<CropResponse> getCropsByFarmerId(Long farmerId) {
        return cropRepository.findByFarmerId(farmerId).stream()
                .map(cropMapper::toDto)
                .toList();
    }
...
        Crop existingCrop = cropRepository.findById(cropId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Crop not found with id: " + cropId
                        ));

        // Fetch category
        Category category = categoryRepository
                .findByCategoryNameIgnoreCase(request.categoryName())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Category not found with name: "
                                        + request.categoryName()
                        ));

        // Check duplicate crop name
        if (!existingCrop.getCropName().equalsIgnoreCase(request.cropName())
                && cropRepository.existsByCropNameIgnoreCase(request.cropName())) {

            throw new ResourceAlreadyExistsException(
                    "Crop already exists with name: "
                            + request.cropName()
            );
        }

        // Update fields
        existingCrop.setCropName(request.cropName());
        existingCrop.setPrice(request.price());
        existingCrop.setQuantity(request.quantity());
        existingCrop.setHarvestDate(request.harvestDate());


        // Update category
        existingCrop.setCategory(category);

        // Update timestamp
        existingCrop.setUpdatedAt(LocalDateTime.now());

        // Save updated crop
        Crop updatedCrop = cropRepository.save(existingCrop);

        // Return response
        return cropMapper.toDto(updatedCrop);
    }

    @Override
    public String deleteCropById(Long Id) {
        if(!cropRepository.existsById(Id)){
            throw new ResourceNotFoundException("Crop Not Found");
        }
        cropRepository.deleteById(Id);
        return "Crop deleted successfully";
    }

    @Override
    public CropResponse getCropById(Long Id) {
        Crop crop = cropRepository.findById(Id).orElseThrow(()-> new ResourceNotFoundException("Crop Not Found"));

        return cropMapper.toDto(crop);
    }

    @Override
    public List<CropResponse> getCropsByCategoryId(Long categoryId) {
        List<Crop> crops = cropRepository.findByCategoryId(categoryId);
        List<CropResponse> cropResponses = new ArrayList<>();
        crops.stream().forEach((crop -> cropResponses.add(cropMapper.toDto(crop))));
        return cropResponses;
    }

    @Override
    public List<CropResponse> getAllCrops() {

        return cropRepository.findAll().stream().map(crop -> cropMapper.toDto(crop)).toList();
    }

    @Transactional
    @Override
    public CropResponse uploadCropImage(Long cropId, MultipartFile file) {
        Crop crop = cropRepository.findById(cropId).orElseThrow(()-> new ResourceNotFoundException("Crop Not Found"));
        try{
            if(file == null || file.isEmpty()){
                throw new RuntimeException("File is Empty");
            }
            Path uploadPath = Paths.get(UPLOAD_DIR);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

            Path filePath = uploadPath.resolve(fileName);

            Files.copy(file.getInputStream(), filePath);

            crop.setImageUrl("/"+UPLOAD_DIR+fileName);
        }catch(Exception e){
            e.printStackTrace();
            throw new RuntimeException("Image Upload failed "+ e);
        }
        return cropMapper.toDto(cropRepository.save(crop));
    }
}

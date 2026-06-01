package com.farmconnect.crop.service;

import com.farmconnect.crop.dto.CreateCropRequest;
import com.farmconnect.crop.dto.CropResponse;
import com.farmconnect.crop.entity.Crop;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CropService {
    public CropResponse addCrop(CreateCropRequest crop);
    public CropResponse updateCrop(Long cropId,CreateCropRequest crop);
    public String deleteCropById(Long Id);
    public CropResponse getCropById(Long Id);
    public List<CropResponse> getCropsByCategoryId(Long categoryId);
    public List<CropResponse> getAllCrops();
    public List<CropResponse> getCropsByFarmerId(Long farmerId);
    public CropResponse uploadCropImage(Long cropId, MultipartFile file);
}

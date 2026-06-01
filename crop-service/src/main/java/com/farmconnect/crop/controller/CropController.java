package com.farmconnect.crop.controller;

import com.farmconnect.crop.dto.ApiResponse;
import com.farmconnect.crop.dto.CreateCropRequest;
import com.farmconnect.crop.dto.CropResponse;
import com.farmconnect.crop.service.CropService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/crop")
@RequiredArgsConstructor
public class CropController {

    private  final CropService cropService;

    @PostMapping("/addcrop")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<CropResponse> addCrop(@RequestBody @Valid CreateCropRequest crop){
        CropResponse response = cropService.addCrop(crop);
        return ApiResponse.success("Crop added successfully", HttpStatus.CREATED.value(), response);
    }
    @PutMapping("/updatecrop/{cropId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ApiResponse<CropResponse> updateCrop(@PathVariable Long cropId,@RequestBody @Valid CreateCropRequest crop){
        CropResponse response = cropService.updateCrop(cropId,crop);
        return ApiResponse.success("Crop updated successfully", HttpStatus.ACCEPTED.value(), response);
    }
    @DeleteMapping("/deletecrop/{Id}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<String> deleteCropById(@PathVariable Long Id){
        String response = cropService.deleteCropById(Id);
        return ApiResponse.success(response, HttpStatus.OK.value(), null);
    }
    @GetMapping("/getcropby/{Id}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<CropResponse> getCropById(@PathVariable Long Id){
        CropResponse response = cropService.getCropById(Id);
        return ApiResponse.success("Crop fetched successfully", HttpStatus.OK.value(), response);
    }
    @GetMapping("/getcropsby/{categoryId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<List<CropResponse>> getCropsByCategoryId(@PathVariable Long categoryId){
        List<CropResponse> response = cropService.getCropsByCategoryId(categoryId);
        return ApiResponse.success("Crops fetched successfully", HttpStatus.OK.value(), response);
    }
    @GetMapping("/getallcrops")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<List<CropResponse>> getAllCrops(){
        List<CropResponse> response = cropService.getAllCrops();
        return ApiResponse.success("All crops fetched successfully", HttpStatus.OK.value(), response);
    }
    @GetMapping("/farmer/{farmerId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<List<CropResponse>> getCropsByFarmerId(@PathVariable Long farmerId){
        List<CropResponse> response = cropService.getCropsByFarmerId(farmerId);
        return ApiResponse.success("Farmer crops fetched successfully", HttpStatus.OK.value(), response);
    }
    @PutMapping(value = "/uploadimage/{cropId}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<CropResponse> uploadCropImage(@PathVariable Long cropId, @RequestParam MultipartFile file){
        CropResponse response = cropService.uploadCropImage(cropId,file);
        return ApiResponse.success("Image uploaded successfully", HttpStatus.OK.value(), response);
    }

}

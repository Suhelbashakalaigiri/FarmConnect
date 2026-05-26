package com.farmconnect.crop.controller;

import com.farmconnect.crop.dto.ApiResponse;
import com.farmconnect.crop.dto.CategoryResponse;
import com.farmconnect.crop.dto.CreateCategoryRequest;
import com.farmconnect.crop.service.CategoryService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping("/api/category")
@RestController
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<CategoryResponse> createCategory(@RequestBody CreateCategoryRequest createCategoryrequest){
        CategoryResponse response = categoryService.createCategory(createCategoryrequest);
        return ApiResponse.success("Category created successfully", HttpStatus.CREATED.value(), response);
    }
    @GetMapping("/getall")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<List<CategoryResponse>> getAllCategories(){
        List<CategoryResponse> response = categoryService.getAllCategories();
        return ApiResponse.success("Categories fetched successfully", HttpStatus.OK.value(), response);
    }
    @GetMapping("/getbyid/{categoryId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<CategoryResponse> getCategoryById(@PathVariable Long categoryId){
        CategoryResponse response = categoryService.getCategoryById(categoryId);
        return ApiResponse.success("Category fetched successfully", HttpStatus.OK.value(), response);
    }
    @DeleteMapping("/delete/{categoryId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<String> deleteCategoryById(@PathVariable Long categoryId){
        String response = categoryService.deleteCategoryById(categoryId);
        return ApiResponse.success(response, HttpStatus.OK.value(), null);
    }
    @GetMapping("/getbyname/{categoryName}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<CategoryResponse> getCategoryByCatgoryName(@PathVariable String categoryName){
        CategoryResponse response = categoryService.getCategoryByCatgoryName(categoryName);
        return ApiResponse.success("Category fetched successfully", HttpStatus.OK.value(), response);
    }
}

package com.farmconnect.crop.service;

import com.farmconnect.crop.dto.CategoryResponse;
import com.farmconnect.crop.dto.CreateCategoryRequest;
import com.farmconnect.crop.entity.Category;

import java.util.List;

public interface CategoryService {

    public CategoryResponse createCategory(CreateCategoryRequest createCategoryrequest);
    public List<CategoryResponse> getAllCategories();
    public CategoryResponse getCategoryById(Long categoryId);
    public String deleteCategoryById(Long categoryId);
    public CategoryResponse getCategoryByCatgoryName(String categoryName);


}

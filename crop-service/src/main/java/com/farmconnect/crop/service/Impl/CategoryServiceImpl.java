package com.farmconnect.crop.service.Impl;

import com.farmconnect.crop.dto.CategoryResponse;
import com.farmconnect.crop.dto.CreateCategoryRequest;
import com.farmconnect.crop.entity.Category;
import com.farmconnect.crop.exception.ResourceAlreadyExistsException;
import com.farmconnect.crop.exception.ResourceNotFoundException;
import com.farmconnect.crop.mapper.CategoryMapper;
import com.farmconnect.crop.repository.CategoryRepository;
import com.farmconnect.crop.service.CategoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService{

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Transactional
    @Override
    public CategoryResponse createCategory(CreateCategoryRequest request) {
        categoryRepository.findByCategoryNameIgnoreCase(request.categoryName())
                .ifPresent(category -> {
                    throw new ResourceAlreadyExistsException(
                            "Category already exists with name: "
                                    + request.categoryName()
                    );
                });

        Category category = categoryMapper.toEntity(request);

        Category savedCategory = categoryRepository.save(category);

        return categoryMapper.toDto(savedCategory);
    }

    @Override
    public List<CategoryResponse> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryResponse> categoriesResponse = new ArrayList<>();
        for(Category category : categories){
            categoriesResponse.add(categoryMapper.toDto(category));
        }
        return categoriesResponse;
    }

    @Override
    public CategoryResponse getCategoryById(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("Category Not Found with Id "+ categoryId));
        return categoryMapper.toDto(category);
    }

    @Override
    public String deleteCategoryById(Long categoryId) {
        if(!categoryRepository.existsById(categoryId)){
            throw new ResourceNotFoundException("Category Not Found with Id "+ categoryId);
        }
        categoryRepository.deleteById(categoryId);
        return "Category Deleted Successfully";
    }

    @Override
    public CategoryResponse getCategoryByCatgoryName(String categoryName) {
        Category category = categoryRepository.findByCategoryNameIgnoreCase(categoryName)
                .orElseThrow(()-> new ResourceNotFoundException("Category not Found with Name "+ categoryName));

        return categoryMapper.toDto(category);
    }
}

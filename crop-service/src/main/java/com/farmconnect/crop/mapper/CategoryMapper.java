package com.farmconnect.crop.mapper;

import com.farmconnect.crop.dto.CategoryResponse;
import com.farmconnect.crop.dto.CreateCategoryRequest;
import com.farmconnect.crop.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface CategoryMapper {
    CategoryResponse toDto(Category category);
    Category toEntity(CreateCategoryRequest dto);
}

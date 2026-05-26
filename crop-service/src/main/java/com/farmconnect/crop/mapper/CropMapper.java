package com.farmconnect.crop.mapper;

import com.farmconnect.crop.dto.CreateCropRequest;
import com.farmconnect.crop.dto.CropResponse;
import com.farmconnect.crop.entity.Crop;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface CropMapper {

    CropResponse toDto(Crop crop);
    Crop toEntity(CreateCropRequest request);
}

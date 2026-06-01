package com.farmconnect.crop.repository;

import com.farmconnect.crop.entity.Crop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CropRepository extends JpaRepository<Crop,Long>{
    List<Crop> findByCategoryId(Long categoryId);
    List<Crop> findByFarmerId(Long farmerId);
    Optional<Crop> findByCropNameIgnoreCase(String cropName);
    Boolean existsByCropNameIgnoreCase(String cropName);
}

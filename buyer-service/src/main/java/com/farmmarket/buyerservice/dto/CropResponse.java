package com.farmmarket.buyerservice.dto;

import java.math.BigDecimal;

public record CropResponse(
        Long id,
        String name,
        String category,
        BigDecimal pricePerKg,
        Double availableQuantity,
        String farmerName,
        String location
) {
}

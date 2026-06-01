package com.farmmarket.farmerservice.repository;

import com.farmmarket.farmerservice.entity.Farmer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FarmerRepository extends JpaRepository<Farmer, Long> {
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);
    boolean existsByAadhaarNumber(String aadhaarNumber);
    Optional<Farmer> findByEmail(String email);
    Optional<Farmer> findByPhoneNumber(String phoneNumber);
}

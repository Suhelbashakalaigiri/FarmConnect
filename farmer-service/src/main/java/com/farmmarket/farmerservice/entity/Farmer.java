package com.farmmarket.farmerservice.entity;

import com.farmmarket.farmerservice.enums.*;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "farmers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Farmer {

    @Id
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private LocalDate dateOfBirth;

    private String addressLine;

    private String village;

    private String mandal;

    private String district;

    private String state;

    @Column(length = 6)
    private String pincode;

    private Double landArea;

    @Enumerated(EnumType.STRING)
    private LandUnit landUnit;

    @Enumerated(EnumType.STRING)
    private FarmingType farmingType;

    @Column(unique = true, length = 12)
    private String aadhaarNumber;

    private String bankAccountNumber;

    private String ifscCode;

    private String profileImageUrl;

    private boolean profileCompleted = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FarmerStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VerificationStatus verificationStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FarmerAvailabilityStatus availabilityStatus;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String phoneNumber;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @Column(nullable = false)
    private String addressLine;

    @Column(nullable = false)
    private String village;

    @Column(nullable = false)
    private String mandal;

    @Column(nullable = false)
    private String district;

    @Column(nullable = false)
    private String state;

    @Column(nullable = false, length = 6)
    private String pincode;

    @Column(nullable = false)
    private Double landArea;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LandUnit landUnit;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FarmingType farmingType;

    @Column(nullable = false, unique = true, length = 12)
    private String aadhaarNumber;

    @Column(nullable = false)
    private String bankAccountNumber;

    @Column(nullable = false)
    private String ifscCode;

    private String profileImageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FarmerStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VerificationStatus verificationStatus;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}

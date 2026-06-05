package com.farmmarket.buyerservice.entity;

import com.farmmarket.buyerservice.enums.BuyerStatus;
import com.farmmarket.buyerservice.enums.BuyerType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "buyers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Buyer {

    @Id
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String phoneNumber;

    private String companyName;

    private String addressLine;

    private String villageCity;

    private String district;

    private String state;

    private String pincode;

    private boolean profileCompleted = false;

    @Enumerated(EnumType.STRING)
    private BuyerType buyerType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BuyerStatus status;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}

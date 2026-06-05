package com.farmconnect.authservice.service.impl;

import com.farmconnect.authservice.dto.*;
import com.farmconnect.authservice.entity.AuthUser;
import com.farmconnect.authservice.enums.Role;
import com.farmconnect.authservice.exception.*;
import com.farmconnect.authservice.feign.client.BuyerServiceClient;
import com.farmconnect.authservice.feign.client.FarmerServiceClient;
import com.farmconnect.authservice.feign.dto.CreateBuyerProfileRequest;
import com.farmconnect.authservice.feign.dto.CreateFarmerProfileRequest;
import com.farmconnect.authservice.repository.AuthUserRepository;
import com.farmconnect.authservice.security.CurrentUser;
import com.farmconnect.authservice.security.JwtTokenProvider;
import com.farmconnect.authservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final AuthUserRepository authUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final BuyerServiceClient buyerServiceClient;
    private final FarmerServiceClient farmerServiceClient;

    @Override
    @Transactional
    public AuthResponse registerBuyer(RegisterBuyerRequest request) {
        log.info("Registering buyer: {}", request.email());

        if (authUserRepository.existsByEmail(request.email())) {
            throw new UserAlreadyExistsException("User already exists with email: " + request.email());
        }

        // 1. Create Auth User
        AuthUser authUser = AuthUser.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.BUYER)
                .isActive(true)
                .build();

        // Save first to generate ID (Source of Truth)
        AuthUser savedUser = authUserRepository.saveAndFlush(authUser);
        Long generatedId = savedUser.getId();

        // 2. Create Buyer Profile via Feign (Internal Endpoint)
        try {
            ApiResponse<Object> buyerResp = buyerServiceClient.createBuyerProfile(new CreateBuyerProfileRequest(
                    generatedId,
                    request.fullName(),
                    request.email(),
                    request.phone()
            ));

            if (!buyerResp.success()) {
                throw new RegistrationFailedException("Failed to create buyer profile: " + buyerResp.message());
            }
        } catch (Exception e) {
            log.error("Buyer profile creation failed for user {}: {}", generatedId, e.getMessage());
            throw new RegistrationFailedException("Buyer Service communication failure: " + e.getMessage());
        }

        String token = jwtTokenProvider.generateToken(generatedId, savedUser.getEmail(), savedUser.getRole().name());
        return new AuthResponse(token, "refresh_token_" + generatedId, generatedId, savedUser.getEmail(), savedUser.getRole().name());
    }

    @Override
    @Transactional
    public AuthResponse registerFarmer(RegisterFarmerRequest request) {
        log.info("Registering farmer: {}", request.email());

        if (authUserRepository.existsByEmail(request.email())) {
            throw new UserAlreadyExistsException("User already exists with email: " + request.email());
        }

        // 1. Create Auth User
        AuthUser authUser = AuthUser.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.FARMER)
                .isActive(true)
                .build();

        // Save first to generate ID
        AuthUser savedUser = authUserRepository.saveAndFlush(authUser);
        Long generatedId = savedUser.getId();

        // 2. Create Farmer Profile via Feign
        try {
            ApiResponse<Object> farmerResp = farmerServiceClient.createFarmerProfile(new CreateFarmerProfileRequest(
                    generatedId,
                    request.fullName(),
                    request.email(),
                    request.phone()
            ));

            if (!farmerResp.success()) {
                throw new RegistrationFailedException("Failed to create farmer profile: " + farmerResp.message());
            }
        } catch (Exception e) {
            log.error("Farmer profile creation failed for user {}: {}", generatedId, e.getMessage());
            throw new RegistrationFailedException("Farmer Service communication failure: " + e.getMessage());
        }

        String token = jwtTokenProvider.generateToken(generatedId, savedUser.getEmail(), savedUser.getRole().name());
        return new AuthResponse(token, "refresh_token_" + generatedId, generatedId, savedUser.getEmail(), savedUser.getRole().name());
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        AuthUser user = authUserRepository.findByEmail(request.email())
                .orElseThrow(() -> new AuthenticationFailedException("Invalid email or password"));

        user.setLastLogin(LocalDateTime.now());
        authUserRepository.save(user);

        String token = jwtTokenProvider.generateToken(user.getId(), user.getEmail(), user.getRole().name());
        return new AuthResponse(token, "refresh_token_" + user.getId(), user.getId(), user.getEmail(), user.getRole().name());
    }

    @Override
    public CurrentUser validateToken(String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        
        String email = jwtTokenProvider.extractEmail(token);
        AuthUser user = authUserRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidTokenException("Invalid token: User not found"));
        
        if (!jwtTokenProvider.isTokenValid(token, new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), java.util.Collections.emptyList()))) {
            throw new InvalidTokenException("Token is expired or invalid");
        }

        return new CurrentUser(user.getId(), user.getEmail(), user.getRole().name());
    }
}

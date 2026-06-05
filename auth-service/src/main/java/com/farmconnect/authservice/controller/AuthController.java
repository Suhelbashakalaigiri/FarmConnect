package com.farmconnect.authservice.controller;

import com.farmconnect.authservice.dto.*;
import com.farmconnect.authservice.security.CurrentUser;
import com.farmconnect.authservice.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register/buyer")
    public ResponseEntity<ApiResponse<AuthResponse>> registerBuyer(@Valid @RequestBody RegisterBuyerRequest request) {
        AuthResponse response = authService.registerBuyer(request);
        return new ResponseEntity<>(ApiResponse.success("Buyer registered successfully", HttpStatus.CREATED.value(), response), HttpStatus.CREATED);
    }

    @PostMapping("/register/farmer")
    public ResponseEntity<ApiResponse<AuthResponse>> registerFarmer(@Valid @RequestBody RegisterFarmerRequest request) {
        AuthResponse response = authService.registerFarmer(request);
        return new ResponseEntity<>(ApiResponse.success("Farmer registered successfully", HttpStatus.CREATED.value(), response), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success("Login successful", HttpStatus.OK.value(), response));
    }

    @GetMapping("/validate")
    public ResponseEntity<ApiResponse<CurrentUser>> validate(@RequestHeader("Authorization") String token) {
        CurrentUser response = authService.validateToken(token);
        return ResponseEntity.ok(ApiResponse.success("Token is valid", HttpStatus.OK.value(), response));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout() {
        // Stateless JWT logout is handled by client (clearing token)
        return ResponseEntity.ok(ApiResponse.success("Logout successful (clear token on client)", HttpStatus.OK.value(), null));
    }
}

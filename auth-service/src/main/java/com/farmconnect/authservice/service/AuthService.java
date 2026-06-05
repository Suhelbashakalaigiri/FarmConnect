package com.farmconnect.authservice.service;

import com.farmconnect.authservice.dto.*;
import com.farmconnect.authservice.security.CurrentUser;

public interface AuthService {
    AuthResponse registerBuyer(RegisterBuyerRequest request);
    AuthResponse registerFarmer(RegisterFarmerRequest request);
    AuthResponse login(LoginRequest request);
    CurrentUser validateToken(String token);
}

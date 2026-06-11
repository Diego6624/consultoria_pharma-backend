package com.pharma.consultoria_pharma.services;

import com.pharma.consultoria_pharma.dto.request.LoginRequest;
import com.pharma.consultoria_pharma.dto.response.AuthResponse;

public interface AuthService {

    AuthResponse login(LoginRequest request);
}

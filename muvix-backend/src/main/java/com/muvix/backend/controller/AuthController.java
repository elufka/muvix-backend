package com.muvix.backend.controller;
import com.muvix.backend.dto.request.LoginRequest;
import com.muvix.backend.dto.request.RegisterRequest;
import com.muvix.backend.dto.request.RefreshTokenRequest;
import com.muvix.backend.dto.response.AuthResponse;
import com.muvix.backend.dto.response.UserResponse;
import com.muvix.backend.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService service;

    public AuthController(AuthService service){
        this.service=service;
    }
    @PostMapping("/register")
    public AuthResponse register(@Valid @RequestBody RegisterRequest request){
        return service.register(request);
    }
    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request){
        return service.login(request);
    }
    @PostMapping("/refresh-token")
    public AuthResponse refresh(@Valid @RequestBody RefreshTokenRequest request){
        return service.refresh(request);
    }
    @GetMapping("/me")
    public UserResponse me(Authentication authentication){
        return service.currentUser(authentication.getName());
    }
}

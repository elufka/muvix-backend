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
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
@RestController @RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name="Authentication",description="Account registration, login, current profile, and token refresh.")
public class AuthController {
    private final AuthService service;
    @PostMapping("/register")
    @Operation(summary="Register a standard user",description="Creates a non-admin account and returns access and refresh tokens.")
    @ApiResponses({@ApiResponse(responseCode="200",description="Account registered",content=@Content(schema=@Schema(implementation=AuthResponse.class))),@ApiResponse(responseCode="400",description="Invalid registration data"),@ApiResponse(responseCode="409",description="Email is already registered")})
    public AuthResponse register(@Valid @RequestBody RegisterRequest request){
        return service.register(request);
    }
    @PostMapping("/login")
    @Operation(summary="Log in",description="Authenticates with email and password.")
    @ApiResponses({@ApiResponse(responseCode="200",description="Authentication succeeded",content=@Content(schema=@Schema(implementation=AuthResponse.class))),@ApiResponse(responseCode="401",description="Invalid credentials"),@ApiResponse(responseCode="400",description="Invalid request")})
    public AuthResponse login(@Valid @RequestBody LoginRequest request){
        return service.login(request);
    }
    @PostMapping("/refresh-token")
    @Operation(summary="Refresh JWT tokens",description="Exchanges a valid refresh token for a new access and refresh token pair.")
    @ApiResponses({@ApiResponse(responseCode="200",description="Tokens refreshed",content=@Content(schema=@Schema(implementation=AuthResponse.class))),@ApiResponse(responseCode="401",description="Invalid or expired refresh token")})
    public AuthResponse refresh(@Valid @RequestBody RefreshTokenRequest request){
        return service.refresh(request);
    }
    @GetMapping("/me")
    @Operation(summary="Get my profile",security=@SecurityRequirement(name="bearerAuth"))
    @ApiResponses({@ApiResponse(responseCode="200",description="Current user profile",content=@Content(schema=@Schema(implementation=UserResponse.class))),@ApiResponse(responseCode="401",description="Missing or invalid access token")})
    public UserResponse me(Authentication authentication){
        return service.currentUser(authentication.getName());
    }
}

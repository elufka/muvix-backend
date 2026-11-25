package com.muvix.backend.service;
import com.muvix.backend.dto.request.LoginRequest;
import com.muvix.backend.dto.request.RegisterRequest;
import com.muvix.backend.dto.request.RefreshTokenRequest;
import com.muvix.backend.dto.response.AuthResponse;
import com.muvix.backend.dto.response.UserResponse;
import com.muvix.backend.exception.ConflictException;
import com.muvix.backend.mapper.UserMapper;
import com.muvix.backend.model.User;
import com.muvix.backend.repository.UserRepository;
import com.muvix.backend.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository users;
    private final UserMapper mapper;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authManager;
    private final JwtService jwt;


    @Transactional public AuthResponse register(RegisterRequest request){
        String email=request.getEmail().trim().toLowerCase();
        if(users.existsByEmailIgnoreCase(email)) throw new ConflictException("An account with this email already exists");
        request.setEmail(email); User user=mapper.toEntity(request); user.setPasswordHash(encoder.encode(request.getPassword()));
        user=users.save(user); return response(user);
    }
    @Transactional(readOnly=true) public AuthResponse login(LoginRequest request){
        Authentication authentication=authManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail().trim().toLowerCase(),request.getPassword()));
        UserDetails details=(UserDetails)authentication.getPrincipal();
        User user=users.findByEmailIgnoreCase(details.getUsername()).orElseThrow(); return response(user);
    }
    @Transactional(readOnly=true) public UserResponse currentUser(String email){
        return mapper.toResponse(users.findByEmailIgnoreCase(email).orElseThrow());
    }
    @Transactional(readOnly=true) public AuthResponse refresh(RefreshTokenRequest request){
        if(!jwt.isValidRefreshToken(request.getRefreshToken())) throw new org.springframework.security.authentication.BadCredentialsException("Invalid refresh token");
        User user=users.findByEmailIgnoreCase(jwt.subject(request.getRefreshToken())).orElseThrow(()->new org.springframework.security.authentication.BadCredentialsException("Invalid refresh token"));
        if(!user.isActive()) throw new org.springframework.security.authentication.DisabledException("Account is disabled");
        return response(user);
    }
    private AuthResponse response(User user){
        return new AuthResponse(jwt.generateAccessToken(user),jwt.generateRefreshToken(user),jwt.getExpirationMs()/1000);
    }
}

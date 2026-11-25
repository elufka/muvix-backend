package com.muvix.backend.security;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import lombok.RequiredArgsConstructor;
@Configuration @EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtFilter;
    private final AuthEntryPoint entryPoint;
    private final ApiAccessDeniedHandler denied;

    @Bean SecurityFilterChain securityFilterChain(HttpSecurity http)throws Exception {
        return http.csrf(csrf->csrf.disable()).cors(cors->{}).sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .exceptionHandling(e->e.authenticationEntryPoint(entryPoint).accessDeniedHandler(denied))
            .authorizeHttpRequests(a->a.requestMatchers(org.springframework.http.HttpMethod.GET,"/api/v1/boxes/me").authenticated()
                .requestMatchers(org.springframework.http.HttpMethod.GET,"/api/v1/boxes/catalog","/api/v1/boxes/*").permitAll()
                .requestMatchers("/api/v1/auth/me","/api/v1/reservations/**","/api/v1/boxes/me","/api/v1/boxes/me/**","/api/v1/boxes/*/publications/**","/api/v1/boxes/*","/api/v1/boxes").authenticated()
                .requestMatchers("/api/v1/auth/register","/api/v1/auth/login","/api/v1/auth/refresh-token").permitAll()
                .requestMatchers(org.springframework.http.HttpMethod.GET,"/api/v1/publication","/api/v1/publication/*","/api/v1/listings","/api/v1/listings/*").permitAll()
                .requestMatchers("/v3/api-docs/**","/swagger-ui/**","/swagger-ui.html").permitAll()
                .requestMatchers("/api/v1/admin/**").hasRole("ADMIN").anyRequest().authenticated())
            .addFilterBefore(jwtFilter,UsernamePasswordAuthenticationFilter.class).build();
    }
    @Bean PasswordEncoder passwordEncoder(){return new BCryptPasswordEncoder();}
    @Bean AuthenticationManager authenticationManager(AuthenticationConfiguration config)throws Exception{return config.getAuthenticationManager();}
}

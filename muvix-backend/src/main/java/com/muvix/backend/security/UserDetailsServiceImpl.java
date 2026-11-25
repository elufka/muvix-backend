package com.muvix.backend.security;
import com.muvix.backend.model.User;
import com.muvix.backend.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import java.util.List;
import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository users;

    @Override public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user=users.findByEmailIgnoreCase(username).orElseThrow(()->new UsernameNotFoundException("User not found"));
        return org.springframework.security.core.userdetails.User.withUsername(user.getEmail())
                .password(user.getPasswordHash()).disabled(!user.isActive())
                .authorities(user.isAdmin()?List.of(new SimpleGrantedAuthority("ROLE_ADMIN")):List.of()).build();
    }
}

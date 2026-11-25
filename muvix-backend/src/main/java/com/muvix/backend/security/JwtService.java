package com.muvix.backend.security;
import com.muvix.backend.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
@Service
public class JwtService {
    private final SecretKey key;
    private final long expirationMs;

    public JwtService(@Value("${muvix.security.jwt.secret}") String secret, @Value("${muvix.security.jwt.expiration-ms}") long expirationMs) {
        this.key=Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret)); this.expirationMs=expirationMs;
    }

    public String generateAccessToken(User user) { return generate(user,"access",expirationMs); }

    public String generateRefreshToken(User user) { return generate(user,"refresh",2_592_000_000L); }

    private String generate(User user,String tokenUse,long ttl) {
        Instant now=Instant.now();
        return Jwts.builder().subject(user.getEmail()).issuedAt(Date.from(now)).expiration(Date.from(now.plusMillis(ttl)))
                .claim("uid",user.getId()).claim("admin",user.isAdmin()).claim("token_use",tokenUse).signWith(key).compact();
    }
    private io.jsonwebtoken.Claims claims(String token) { return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload(); }
    public String subject(String token) { return claims(token).getSubject(); }
    public boolean isValidAccessToken(String token) { try { return "access".equals(claims(token).get("token_use",String.class)); } catch (RuntimeException ex) { return false; } }
    public boolean isValidRefreshToken(String token) { try { return "refresh".equals(claims(token).get("token_use",String.class)); } catch (RuntimeException ex) { return false; } }
    public long getExpirationMs(){return expirationMs;}
}

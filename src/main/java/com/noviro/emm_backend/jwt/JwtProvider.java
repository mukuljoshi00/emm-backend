package com.noviro.emm_backend.jwt;

import com.noviro.emm_backend.model.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.*;

@Component
public class JwtProvider {
    private final String jwtSecret = "change_this_secret_to_a_long_random_string_for_prod";
    private final long jwtExpirationMs = 86400000; // 1 day
    private final Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes());

    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", user.getEmail());
        claims.put("role", user.getRole().name());
        claims.put("organizationId", user.getOrganizationId() != null ? user.getOrganizationId().toString() : null);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getId().toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(key)
                .compact();
    }

    public String getSubject(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getClaims(String token) {
        return (Map<String, Object>) Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}

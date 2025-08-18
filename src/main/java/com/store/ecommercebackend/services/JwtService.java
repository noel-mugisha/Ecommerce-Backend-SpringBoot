package com.store.ecommercebackend.services;

import com.store.ecommercebackend.entities.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Service
public class JwtService {
    @Value("${spring.jwt.secret}")
    private String secret_key;

    public String generateToken(User user) {
        Map<String, String> claims = new HashMap<>();
        claims.put("name", user.getName());
        claims.put("email", user.getEmail());

        return Jwts.builder()
                .claims(claims)
                .subject(String.valueOf(user.getId()))
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 24hrs
                .signWith(getSigningKey())
                .compact();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64URL.decode(secret_key);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}




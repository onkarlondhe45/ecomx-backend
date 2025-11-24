package com.micro.config;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Service
public class JwtService {

	private final SecretKey key;
	private final long expiration;

	public JwtService(@Value("${jwt.secret}") String secret, @Value("${jwt.expiration}") long expiration) {
//		this.key = Keys.hmacShaKeyFor(secret.getBytes());
		this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)); // Add UTF-8 encoding
		this.expiration = expiration;
	}

	public String generateToken(String username, String role) {
		Date now = new Date();
		Date exp = new Date(now.getTime() + expiration);

		return Jwts.builder().subject(username).claims(Map.of("role", role)).issuedAt(now).expiration(exp)
				.signWith(key, Jwts.SIG.HS256).compact();
	}

	public Claims validateToken(String token) {
		return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
	}

	public String getUsername(String token) {
		return validateToken(token).getSubject();
	}

	public String getRole(String token) {
		return validateToken(token).get("role", String.class);
	}

	@PostConstruct
	public void testKey() {
		System.out.println("JWT Key Length: " + key.getEncoded().length);
		System.out.println("JWT Key (Base64): " + java.util.Base64.getEncoder().encodeToString(key.getEncoded()));
	}

}

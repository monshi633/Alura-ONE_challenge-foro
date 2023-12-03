package com.alura.foro.api.infra.security;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alura.foro.api.domain.user.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

@Service
public class TokenService {
	
	@Value("${api.security.secret}")
    private String apiSecret;
	
	public String generateToken(User user) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(apiSecret);
			return JWT.create()
					.withIssuer("alura")
					.withSubject(user.getUsername())
					.withClaim("id", user.getId())
					.withExpiresAt(generateExpiringDate())
					.sign(algorithm);
		} catch (JWTCreationException e){
			throw new RuntimeException("Failed to generate token");
		}
	}

	public String getSubject(String token) {
		if (token == null) {
			throw new RuntimeException("Token is null");
		}
		
		DecodedJWT verifier = null;
//		Validate sign
		try {
			Algorithm algorithm = Algorithm.HMAC256(apiSecret);
            verifier = JWT.require(algorithm)
                    .withIssuer("alura")
                    .build()
                    .verify(token);
            verifier.getSubject();
		} catch (JWTVerificationException e) {
			System.out.println(e.toString()); //This could be improved so it returns "invalid token" or "token expired" in the body and not in console
		}
		
		if (verifier.getSubject() == null) {
			throw new RuntimeException("Invalid verifier");
		}
		return verifier.getSubject();
	}
	
	private Instant generateExpiringDate() {
		return LocalDateTime.now().plusHours(6).toInstant(ZoneOffset.of("-03:00"));
	}

}

package com.azad.basicecommerce.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {

    @Value("${jwt_secret}")
    private String SECRET;

    public String generateJwtToken(String usernameOrEmail) {
        return JWT.create()
                .withSubject("User Details")
                .withClaim("UserID", usernameOrEmail)
                .withIssuedAt(new Date())
                .withIssuer("com.azad.basicecommerce")
                .sign(Algorithm.HMAC256(SECRET));
    }

    public String validateJwtTokenAndRetrieveClaim(String token) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET))
                .withSubject("User Details")
                .withIssuer("com.azad.basicecommerce")
                .build();

        DecodedJWT jwt = verifier.verify(token);
        return jwt.getClaim("UserID").asString();
    }
}

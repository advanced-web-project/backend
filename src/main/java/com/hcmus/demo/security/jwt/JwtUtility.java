package com.hcmus.demo.security.jwt;

import java.util.Date;

import javax.crypto.spec.SecretKeySpec;

import com.hcmus.demo.model.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Utility class for handling JWT operations.
 * This class provides methods for generating and validating JWT tokens.
 */
@Component
@Getter
@Setter
public class JwtUtility {

    private static final String SECRET_KEY_ALGORITHM = "HmacSHA512";

    @Value("${app.security.jwt.issuer}")
    private String issuerName; // The issuer name for the JWT

    @Value("${app.security.jwt.secret}")
    private String secretKey; // The secret key used for signing the JWT

    @Value("${app.security.jwt.access-token.expiration}")
    private int accessTokenExpiration; // The expiration time for the access token in minutes

    /**
     * Generates an access token for the given user.
     *
     * @param user the user for whom the access token is generated
     * @return the generated access token
     * @throws IllegalArgumentException if the user object or its fields have null values
     */
    public String generateAccessToken(User user) {
        if (user == null || user.getId() == null || user.getUsername() == null) {
            throw new IllegalArgumentException("user object is null or its fields have null values");
        }

        long expirationTimeInMillis = System.currentTimeMillis() + 30000;
        String subject = String.format("%s,%s", user.getId(), user.getUsername());

        return Jwts.builder()
                .subject(subject)
                .issuer(issuerName)
                .issuedAt(new Date())
                .expiration(new Date(expirationTimeInMillis))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), Jwts.SIG.HS512)
                .compact();
    }

    /**
     * Validates the given access token.
     *
     * @param token the access token to validate
     * @return the claims extracted from the token
     * @throws JwtValidationException if the token is invalid or expired
     */
    public Claims validateAccessToken(String token) throws JwtValidationException {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(), SECRET_KEY_ALGORITHM);
            return Jwts.parser()
                    .verifyWith(keySpec)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException ex) {
            throw new JwtValidationException("Access token expired", ex);
        } catch (IllegalArgumentException ex) {
            throw new JwtValidationException("Access token is illegal", ex);
        } catch (MalformedJwtException ex) {
            throw new JwtValidationException("Access token is not well formed", ex);
        } catch (UnsupportedJwtException ex) {
            throw new JwtValidationException("Access token is not supported", ex);
        }
    }
}
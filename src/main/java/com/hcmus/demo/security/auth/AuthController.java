package com.hcmus.demo.security.auth;

import com.hcmus.demo.security.CustomUserDetail;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for handling authentication requests.
 * This controller provides endpoints for user login and token generation.
 */
@RestController
@RequestMapping("/auth")
@Validated
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenService tokenService;

    /**
     * Authenticates the user and generates an access token.
     *
     * @param request the authentication request containing username and password
     * @return a ResponseEntity containing the authentication response with the access token
     */
    @PostMapping("/login")
    public ResponseEntity<?> getAccessToken(@RequestBody @Valid AuthRequest request) {
        String username = request.getUsername();
        String password = request.getPassword();

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));

        CustomUserDetail userDetails = (CustomUserDetail) authentication.getPrincipal();
        AuthResponse response = tokenService.generateToken(userDetails.getUser());

        return ResponseEntity.ok(response);
    }

    /**
     * Generates a new access token using a refresh token.
     *
     * @param request the refresh token request containing the refresh token
     * @return a ResponseEntity containing the authentication response with the new access token
     */
    @PostMapping("/refresh-token")
    public ResponseEntity<?> getRefreshToken(@RequestBody @Valid RefreshTokenRequest request) {
        try {
            AuthResponse authResponse = tokenService.refreshTokens(request);
            return ResponseEntity.ok(authResponse);
        } catch (Exception e) {
            // Handle exception and return unauthorized status
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

}
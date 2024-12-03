package com.hcmus.demo.security.auth;

import com.hcmus.demo.security.CustomUserDetail;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    @Autowired
    private  AuthenticationService authenticationService;

    /**
     * Authenticates the user and generates an access token.
     *
     * @param request the authentication request containing username and password
     * @return a ResponseEntity containing the authentication response with the access token
     */
    @PostMapping("/login")
    public AuthResponse getAccessToken(@RequestBody @Valid AuthRequest request) {
        String username = request.getUsername();
        String password = request.getPassword();

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));

        CustomUserDetail userDetails = (CustomUserDetail) authentication.getPrincipal();

        return tokenService.generateToken(userDetails.getUser());
    }

    /**
     * Handles POST requests for Google login authentication.
     *
     * @param code the authorization code from Google's OAuth 2.0 flow.
     * @return a ResponseEntity containing the authentication result (e.g., user info or token).
     * @throws Exception if authentication fails or communication with Google encounters an issue.
     *
     * This method exchanges the provided code for access and ID tokens via the
     * `authenticationService`, validates the user's identity, and returns the result.
     */
    @PostMapping("/outbound/authentication")
    ResponseEntity<?> outboundAuthenticate(@RequestParam("code") String code) throws Exception {
        var result = authenticationService.outboundAuthentication(code);
        return ResponseEntity.ok().body(result);
    }

    /**
     * Generates a new access token using a refresh token.
     *
     * @param request the refresh token request containing the refresh token
     * @return a ResponseEntity containing the authentication response with the new access token
     */
    @PostMapping("/refresh-token")
    public AuthResponse getRefreshToken(@RequestBody @Valid RefreshTokenRequest request)  {
        try {
            return tokenService.refreshTokens(request);
        } catch (Exception e) {
            // Handle exception and return unauthorized status
            throw new BadCredentialsException("Invalid refresh token");
        }
    }

}
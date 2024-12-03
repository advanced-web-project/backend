package com.hcmus.demo.security.auth;

import com.hcmus.demo.exception_handler.exception.RefreshTokenExpireException;
import com.hcmus.demo.exception_handler.exception.RefreshTokenNotFoundException;
import com.hcmus.demo.model.RefreshToken;
import com.hcmus.demo.model.User;
import com.hcmus.demo.security.jwt.JwtUtility;
import com.hcmus.demo.token.RefreshTokenRepository;
import com.hcmus.demo.user.UserResponseDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Service class for handling token generation and refresh operations.
 * This class uses JwtUtility to generate access tokens for authenticated users
 * and manages refresh tokens for token renewal.
 */
@Service
public class TokenService {
    @Autowired
    private JwtUtility jwtUtil;

    @Autowired
    private ModelMapper modelMapper;

    @Value("${app.security.jwt.refresh-token.expiration}")
    private int refreshTokenExpiration;

    @Autowired
    RefreshTokenRepository refreshTokenRepo;

    @Autowired
    PasswordEncoder passwordEncoder;

    /**
     * Generates an access token and a refresh token for the given user.
     * The access token is used for authentication, while the refresh token
     * is used to obtain a new access token when the current one expires.
     *
     * @param user the user for whom the tokens are generated
     * @return an AuthResponse containing the generated access and refresh tokens
     */
    public AuthResponse generateToken(User user) {
        String accessToken = jwtUtil.generateAccessToken(user);

        AuthResponse response = new AuthResponse();
        response.setAccessToken(accessToken);

        String randomUUID = UUID.randomUUID().toString();
        response.setRefreshToken(randomUUID);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setU(user);
        refreshToken.setToken(passwordEncoder.encode(randomUUID));

        long refreshTokenExpirationInMillis = System.currentTimeMillis() + refreshTokenExpiration * 60000L;
        refreshToken.setExpiryTime(new Date(refreshTokenExpirationInMillis));

        refreshTokenRepo.save(refreshToken);

        UserResponseDTO userDTO = modelMapper.map(user, UserResponseDTO.class);
        response.setUser(userDTO);

        return response;
    }

    /**
     * Refreshes the access token using the provided refresh token.
     * Validates the refresh token and generates a new access token if the refresh token is valid.
     *
     * @param request the refresh token request containing the username and refresh token
     * @return an AuthResponse containing the new access token
     * @throws RefreshTokenNotFoundException if the refresh token is not found
     * @throws RefreshTokenExpireException if the refresh token has expired
     */
    public AuthResponse refreshTokens(RefreshTokenRequest request) throws RefreshTokenNotFoundException, RefreshTokenExpireException {
        AuthResponse response = new AuthResponse();

        String username = request.getUsername();
        String rawRefreshToken = request.getRefreshToken();

        System.out.println("Username: " + username);
        System.out.println("Refresh token: " + rawRefreshToken);

        RefreshToken refreshTokenFound = null;
        List<RefreshToken> refreshTokens = refreshTokenRepo.findByUsername(username);
        for (RefreshToken refreshToken : refreshTokens) {
            if (passwordEncoder.matches(rawRefreshToken, refreshToken.getToken())) {
                refreshTokenFound = refreshToken;
            }
        }

        System.out.println("Refresh token found: " + refreshTokenFound);

        if (refreshTokenFound == null) {
            throw new RefreshTokenNotFoundException("Refresh token not found");
        }

        Date currentTime = new Date();
        if (refreshTokenFound.getExpiryTime().before(currentTime)) {
            throw new RefreshTokenExpireException("Refresh token has expired");
        }

        response = generateToken(refreshTokenFound.getU());
        return response;
    }
}
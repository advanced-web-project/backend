package com.hcmus.demo.security.auth;

import com.hcmus.demo.model.User;
import com.hcmus.demo.security.CustomUserDetail;
import com.hcmus.demo.security.httpclient.OutboundIdentityClient;
import com.hcmus.demo.security.httpclient.OutboundUserClient;
import com.hcmus.demo.user.UserService;
import lombok.RequiredArgsConstructor;

import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

/**
 * Service class for handling authentication operations.
 * This class provides methods for authenticating users with an external identity provider and managing user tokens.
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private  final OutboundIdentityClient outboundIdentityClient;
    private final OutboundUserClient outboundUserClient;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    @NonFinal
    @Value("${outbound.identity.client-id}")
    protected  String CLIENT_ID;

    @NonFinal
    @Value("${outbound.identity.client-secret}")
    protected String CLIENT_SECRET;

    @NonFinal
    @Value("${redirect-uri}")
    protected String REDIRECT_URI;

    @NonFinal
    protected String GRANT_TYPE = "authorization_code";


    /**
     * Authenticates a user using an authorization code.
     * This method exchanges the authorization code for an access token, retrieves user information, and generates a token for the user.
     *
     * @param code the authorization code received from the external identity provider
     * @return the authentication response containing the generated token
     * @throws Exception if an error occurs during the authentication process
     */
    public AuthResponse outboundAuthentication(String code) throws Exception {
        var response = outboundIdentityClient.exchangeToken(ExchangeTokenRequest.builder()
                .code(code)
                .clientId(CLIENT_ID)
                .clientSecret(CLIENT_SECRET)
                .redirectUri(REDIRECT_URI)
                .grantType(GRANT_TYPE)
                .build());
        OutboundUserResponse userResponse = outboundUserClient.getUserInfo("json",response.getAccessToken());



        if(!userService.checkUniqueEmail(userResponse.getEmail()))
        {
            // Update user profile
            User user = userService.getUserByEmail(userResponse.getEmail());
            user.setProfile(userResponse.getPicture());

            return tokenService.generateToken(user);
        }
        User user = User.builder()
                .email(userResponse.getEmail())
                .username(userResponse.getName())
                .password("123456")
                .profile(userResponse.getPicture())
                .build();
        userService.saveUser(user);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userResponse.getName(),"123456"));
        CustomUserDetail userDetails = (CustomUserDetail) authentication.getPrincipal();
        return tokenService.generateToken(userDetails.getUser());
    }
}

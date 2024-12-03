package com.hcmus.demo.security.auth;

import com.hcmus.demo.model.User;
import com.hcmus.demo.security.CustomUserDetail;
import com.hcmus.demo.security.httpclient.OutboundIdentityClient;
import com.hcmus.demo.security.httpclient.OutboundUserClient;
import com.hcmus.demo.user.UserRequestDTO;
import com.hcmus.demo.user.UserService;
import lombok.RequiredArgsConstructor;

import lombok.experimental.NonFinal;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private  final OutboundIdentityClient outboundIdentityClient;
    private final OutboundUserClient outboundUserClient;
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    @Autowired
    private final TokenService tokenService;
    @NonFinal
    @Value("${outbound.identity.client-id}")
    protected  String CLIENT_ID;
    @NonFinal
    @Value("${outbound.identity.client-secret}")
    protected String CLIENT_SECRET;
    @NonFinal
    protected String REDIRECT_URI = "http://localhost:3000/authenticate";
    @NonFinal
    protected String GRANT_TYPE = "authorization_code";
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
            return tokenService.generateToken(userService.getUserByEmail(userResponse.getEmail()));
        }
        UserRequestDTO userDto = UserRequestDTO.builder()
                .email(userResponse.getEmail())
                .address("VN")
                .password("123456")
                .firstName(userResponse.getFamilyName())
                .lastName(userResponse.getGivenName())
                .username(userResponse.getName()).build();
        User user = dtoToEntity(userDto);
        userService.saveUser(user);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userResponse.getName(),"123456"));

        CustomUserDetail userDetails = (CustomUserDetail) authentication.getPrincipal();
        return tokenService.generateToken(userDetails.getUser());
    }
    public User dtoToEntity(UserRequestDTO userDto) {
        return modelMapper.map(userDto, User.class);
    }
}

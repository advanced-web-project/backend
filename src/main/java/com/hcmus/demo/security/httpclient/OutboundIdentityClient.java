package com.hcmus.demo.security.httpclient;

import com.hcmus.demo.security.auth.ExchangeTokenRequest;
import com.hcmus.demo.security.auth.ExchangeTokenResponse;
import feign.QueryMap;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * Feign client interface for interacting with the external identity provider.
 * This interface defines methods for making HTTP requests to the identity provider's API.
 */
@FeignClient(name = "outbound-identity", url = "https://oauth2.googleapis.com")
public interface OutboundIdentityClient {

    /**
     * Exchanges an authorization code for an access token.
     * This method sends a POST request to the identity provider's token endpoint.
     *
     * @param request the request object containing the authorization code and other required parameters
     * @return the response object containing the access token and other related information
     */
    @PostMapping(value = "/token", produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    ExchangeTokenResponse exchangeToken(@QueryMap ExchangeTokenRequest request);
}
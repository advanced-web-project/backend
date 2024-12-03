package com.hcmus.demo.security.httpclient;

import com.hcmus.demo.security.auth.ExchangeTokenRequest;
import com.hcmus.demo.security.auth.ExchangeTokenResponse;
import feign.QueryMap;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.cloud.openfeign.FeignClient;
@FeignClient(name = "outbound-identity", url = "https://oauth2.googleapis.com")
public interface OutboundIdentityClient {
    @PostMapping(value = "/token", produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    ExchangeTokenResponse exchangeToken(@QueryMap ExchangeTokenRequest request);
}
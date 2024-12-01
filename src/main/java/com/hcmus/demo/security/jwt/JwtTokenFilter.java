package com.hcmus.demo.security.jwt;

import java.io.IOException;

import com.hcmus.demo.model.User;
import com.hcmus.demo.security.CustomUserDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Filter class for JWT token validation.
 * This filter intercepts HTTP requests to validate the JWT token and set the authentication context.
 */
@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenFilter.class);

    @Autowired
    JwtUtility jwtUtil;

    @Autowired
    @Qualifier("handlerExceptionResolver")
    HandlerExceptionResolver exceptionResolver;

    /**
     * Filters incoming requests to validate JWT tokens.
     * If the token is valid, sets the authentication context.
     *
     * @param request     the HTTP request
     * @param response    the HTTP response
     * @param filterChain the filter chain
     * @throws ServletException if a servlet error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (!hasAuthorizationBearer(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = getBearerToken(request);

        LOGGER.info("Token: " + token);

        try {
            Claims claims = jwtUtil.validateAccessToken(token);
            UserDetails userDetails = getUserDetails(claims);

            setAuthenticationContext(userDetails, request);

            LOGGER.info("Next filter");
            filterChain.doFilter(request, response);

            clearAuthenticationContext();

        } catch (JwtValidationException e) {
            LOGGER.error(e.getMessage(), e);
            exceptionResolver.resolveException(request, response, null, e);
        }

    }

    /**
     * Clears the authentication context.
     */
    private void clearAuthenticationContext() {
        SecurityContextHolder.clearContext();
    }

    /**
     * Sets the authentication context with the given user details.
     *
     * @param userDetails the user details
     * @param request     the HTTP request
     */
    private void setAuthenticationContext(UserDetails userDetails, HttpServletRequest request) {
        var authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    /**
     * Extracts user details from the JWT claims.
     *
     * @param claims the JWT claims
     * @return the user details
     */
    private UserDetails getUserDetails(Claims claims) {
        String subject = (String) claims.get(Claims.SUBJECT);
        String[] array = subject.split(",");

        Long userId = Long.valueOf(array[0]);
        String username = array[1];

        User user = new User();
        user.setId(userId);
        user.setUsername(username);

        return new CustomUserDetail(user);
    }

    /**
     * Checks if the request has an Authorization header with a Bearer token.
     *
     * @param request the HTTP request
     * @return true if the request has a Bearer token, false otherwise
     */
    private boolean hasAuthorizationBearer(HttpServletRequest request) {
        String header = request.getHeader("Authorization");

        LOGGER.info("Authorization Header: " + header);

        if (ObjectUtils.isEmpty(header) || !header.startsWith("Bearer")) {
            return false;
        }
        return true;
    }

    /**
     * Extracts the Bearer token from the Authorization header.
     *
     * @param request the HTTP request
     * @return the Bearer token, or null if not present
     */
    private String getBearerToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");

        String[] array = header.split(" ");
        if (array.length == 2) return array[1];
        return null;
    }

}
package com.hcmus.demo.security;

import com.hcmus.demo.security.jwt.JwtTokenFilter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

/**
 * Configuration class for Spring Security.
 * This class sets up the security filter chain, authentication providers, and other security-related beans.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    @Autowired
    JwtTokenFilter jwtFilter;

    private final CorsConfigurationSource corsConfigurationSource;

    /**
     * Bean for password encoding using BCrypt.
     *
     * @return the password encoder
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Bean for custom user details service.
     *
     * @return the user details service
     */
    @Bean
    UserDetailsService userDetailsService() {
        return new CustomUserDetailService();
    }

    /**
     * Bean for DAO authentication provider.
     * This provider uses the custom user details service and password encoder.
     *
     * @return the DAO authentication provider
     */
    @Bean
    DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setPasswordEncoder(passwordEncoder());
        authProvider.setUserDetailsService(userDetailsService());

        return authProvider;
    }

    /**
     * Bean for authentication manager.
     *
     * @param authConfig the authentication configuration
     * @return the authentication manager
     * @throws Exception if an error occurs while getting the authentication manager
     */
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    /**
     * Configures the security filter chain.
     * Sets up CORS, CSRF, exception handling, and JWT filter.
     *
     * @param http the HTTP security configuration
     * @return the security filter chain
     * @throws Exception if an error occurs while configuring the security filter chain
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/user/register", "/user/check-unique-email/**", "/user/check-unique-username/**").permitAll()
                                .requestMatchers("/auth/**").permitAll()
                                .requestMatchers("/user/**").authenticated()
                                .anyRequest().permitAll())
                .csrf(csrf -> csrf.disable())
                .exceptionHandling(exh -> exh.authenticationEntryPoint(
                        (request, response, exception) -> {
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, exception.getMessage());
                        }))
                .addFilterBefore(jwtFilter, AuthorizationFilter.class);

        return http.build();
    }
}
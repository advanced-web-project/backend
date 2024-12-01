package com.hcmus.demo.security;

/**
 * Custom implementation of UserDetails.
 * This class is used to encapsulate the user details required by Spring Security.
 */
import com.hcmus.demo.model.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Setter
@Getter
public class CustomUserDetail implements UserDetails {

    private User user;

    /**
     * Constructor to initialize CustomUserDetail with a User object.
     *
     * @param user the user object
     */
    public CustomUserDetail(User user) {
        this.user = user;
    }

    /**
     * Returns the authorities granted to the user.
     *
     * @return a collection of granted authorities
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    /**
     * Returns the password used to authenticate the user.
     *
     * @return the password
     */
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * Returns the username used to authenticate the user.
     *
     * @return the username
     */
    @Override
    public String getUsername() {
        return user.getUsername();
    }
}
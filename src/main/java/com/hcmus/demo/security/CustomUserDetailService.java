package com.hcmus.demo.security;

import com.hcmus.demo.model.User;
import com.hcmus.demo.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Service class for loading user-specific data.
 * This class implements the UserDetailsService interface to provide a custom user details service.
 */
@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository repo;

    private static final String USER_NOT_FOUND_MSG = "User with username %s not found";

    /**
     * Loads the user by username.
     * This method is used by Spring Security to retrieve user details during authentication.
     *
     * @param username the username identifying the user whose data is required
     * @return a fully populated user record (never null)
     * @throws UsernameNotFoundException if the user could not be found or the user has no GrantedAuthority
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User u = repo.findByUsername(username);
        if (u == null) {
            throw new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, username));
        }
        return new CustomUserDetail(u);
    }

}
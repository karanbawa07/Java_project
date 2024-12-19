package com.example.webapp.Service;

import com.example.webapp.models.User;
import com.example.webapp.models.Authority;
import com.example.webapp.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Fetch the user from the database
        User user = userRepository.findByUsername(username);

        // Throw exception if the user is not found
        if (user == null) {
            throw new UsernameNotFoundException("User with username '" + username + "' not found");
        }

        // Build and return the UserDetails object
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername()) // Set username
                .password(user.getPassword()) // Set encoded password
                .authorities(user.getAuthorities().stream()
                        .map(Authority::getRole) // Map Authority to roles (e.g., "ROLE_USER")
                        .toArray(String[]::new)) // Convert to array
                .accountLocked(!user.isEnabled()) // Lock account if user is disabled
                .build();
    }
}

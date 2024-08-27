package com.example.BookHealthServiceOnline.config;

import com.example.BookHealthServiceOnline.dao.UserDao;
import com.example.BookHealthServiceOnline.domain.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;

@Configuration
public class ApplicationConfiguration {
    private final UserDao userDao;

    public ApplicationConfiguration(UserDao userDao) {
        this.userDao = userDao;
    }

    @Bean
    UserDetailsService userDetailsService() {
        return username -> {
            // Retrieve the User entity from the database
            User user = userDao.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            // Convert the User entity to UserDetails
            return new org.springframework.security.core.userdetails.User(
                    user.getUsername(),
                    user.getPassword(),
                    // Assuming the user has authorities like roles, otherwise, you can pass an empty list or set
                    new ArrayList<>()
            );
        };
    }

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) { // Method injection here
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }
}

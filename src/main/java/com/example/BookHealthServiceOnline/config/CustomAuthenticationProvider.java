//package com.example.BookHealthServiceOnline.config;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//
//@Component
//public class CustomAuthenticationProvider implements AuthenticationProvider {
//
//    private final UserDetailsService userDetailsService;
//    private final PasswordEncoder passwordEncoder;
//
//    public CustomAuthenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
//        this.userDetailsService = userDetailsService;
//        this.passwordEncoder = passwordEncoder;
//    }
//
//    @Override
//    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//        String username = authentication.getName();
//        String password = authentication.getCredentials().toString();
//
//        // Custom filter logic here (e.g., setting tenant context)
//        // This is not the recommended place to do filtering, but it can be done
//
//        if (tenantHeader != null) {
//            AppTenantContext.setCurrentTenant(tenantHeader);
//        }
//
//        UserDetails user = userDetailsService.loadUserByUsername(username);
//
//        if (passwordEncoder.matches(password, user.getPassword())) {
//            return new UsernamePasswordAuthenticationToken(user, password, user.getAuthorities());
//        } else {
//            throw new AuthenticationException("Authentication failed for user: " + username) {};
//        }
//    }
//
//    @Override
//    public boolean supports(Class<?> authentication) {
//        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
//    }
//}

package com.example.BookHealthServiceOnline.config;

import com.example.BookHealthServiceOnline.dao.AdminUserDao;
import com.example.BookHealthServiceOnline.dao.UserDao;
import com.example.BookHealthServiceOnline.domain.AdminUser;
import com.example.BookHealthServiceOnline.domain.User;
import com.example.BookHealthServiceOnline.service.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


import static com.example.BookHealthServiceOnline.config.AppTenantContext.DEFAULT_TENANT;
import static com.example.BookHealthServiceOnline.config.AppTenantContext.PRIVATE_TENANT_HEADER;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final HandlerExceptionResolver handlerExceptionResolver;
    private static final String ADMIN = "ADMIN";
    private static final String HOSPITALADMIN = "HOSPITAL_ADMIN";
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final UserDao userDao;
    private final AppTenantContext appTenantContext;
    private final AdminUserDao adminUserDao;

    public JwtAuthenticationFilter(
            JwtService jwtService,
            UserDetailsService userDetailsService,
            HandlerExceptionResolver handlerExceptionResolver, UserDao userDao, AppTenantContext appTenantContext, AdminUserDao adminUserDao
    ) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.handlerExceptionResolver = handlerExceptionResolver;
        this.userDao = userDao;
        this.appTenantContext = appTenantContext;
        this.adminUserDao = adminUserDao;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            String privateTenant = request.getHeader(PRIVATE_TENANT_HEADER);
            if (privateTenant != null) {
                AppTenantContext.setCurrentTenant(privateTenant);
            }
            filterChain.doFilter(request, response);
            return;
        }

        try {
            final String jwt = authHeader.substring(7);
            final String username = jwtService.extractUsername(jwt);
            String privateTenant = request.getHeader(PRIVATE_TENANT_HEADER);
            AppTenantContext.setCurrentTenant(privateTenant);
            Claims claims=jwtService.extractClaims(jwt);
            // Extract the role from claims
            String role = claims.get("ROLE", String.class);
            if (role.equals(ADMIN) || role.equals(HOSPITALADMIN)) {

                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

                if (username != null && authentication == null) {
                    AdminUser adminUser = this.adminUserDao.findByUsername(username).orElse(null);
                    if (adminUser!=null) {
                        if (jwtService.isTokenValid(jwt,adminUser )) {


                            // Convert the role into GrantedAuthority
                            List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));
                            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                    adminUser,
                                    null,
                                    authorities
                            );

                            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                            SecurityContextHolder.getContext().setAuthentication(authToken);
                        }
                    }


                }
            }else {

                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

                if (username != null && authentication == null) {
                    User user = this.userDao.findByUsername(username).orElse(null);
                    if (user!=null) {
                        if (jwtService.isTokenValid(jwt, user)) {

                            // Convert the role into GrantedAuthority
                            List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));
                            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                    user,
                                    null,
                                    authorities
                            );

                            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                            SecurityContextHolder.getContext().setAuthentication(authToken);
                        }
                    }

                }
            }


            filterChain.doFilter(request, response);
        } catch (Exception exception) {
            handlerExceptionResolver.resolveException(request, response, null, exception);
        }
    }
}
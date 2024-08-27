package com.example.BookHealthServiceOnline.config;


import io.micrometer.common.lang.NonNull;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Component
public class AppTenantContextFilter{

    private static final String LOGGER_TENANT_ID = "tenant_id";
    public static final String PRIVATE_TENANT_HEADER = "X-PrivateTenant";
    private static final String DEFAULT_TENANT = "public";
    private static final ThreadLocal<String> currentTenant = new InheritableThreadLocal<>();

    public static String getCurrentTenant() {
        String tenant = currentTenant.get();
        return Objects.requireNonNullElse(tenant, DEFAULT_TENANT);
    }

    public static void setCurrentTenant(String tenant) {
        MDC.put(LOGGER_TENANT_ID, tenant);
        currentTenant.set(tenant);
    }

    public static void clear() {
        MDC.clear();
        currentTenant.remove();
    }
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse res = (HttpServletResponse) servletResponse;
        String privateTenant = req.getHeader(PRIVATE_TENANT_HEADER);

        if (privateTenant != null) {
            AppTenantContextFilter.setCurrentTenant(privateTenant);
        }
        try {
            filterChain.doFilter(req, res);
        } finally {
            AppTenantContextFilter.clear(); // Ensure tenant context is cleared after request
        }
    }
}

package com.example.BookHealthServiceOnline.config;


import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;


@Component
public class AppTenantContext implements Filter {
    private static final String LOGGER_TENANT_ID = "tenant_id";
    public static final String PRIVATE_TENANT_HEADER = "X-PrivateTenant";
    public static final String DEFAULT_TENANT = "public";
    private static final ThreadLocal<String> currentTenant = new InheritableThreadLocal<>();
    private static final ThreadLocal<String> currentServerUrl = new InheritableThreadLocal<>();

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

    public static String getServerUrl() {
        return currentServerUrl.get();
    }

    public static void setServerUrl(String serverUrl) {
        currentServerUrl.set(serverUrl);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException, ServletException, IOException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String privateTenant = req.getHeader(PRIVATE_TENANT_HEADER);
        if (privateTenant != null) {
            AppTenantContext.setCurrentTenant(privateTenant);
        }
        chain.doFilter(request, response);
    }
}
package com.marketflow.config;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;

@Component
public class ApiKeyFilter extends OncePerRequestFilter {

    @Value("${api.key}")
    private String validKey;

    private static final Set<String> PUBLIC_PATHS = Set.of(
            "/swagger.html",
            "/swagger-ui",
            "/swagger-ui/",
            "/swagger-ui/swagger-ui.css",
            "/swagger-ui/swagger-ui-bundle.js",
            "/swagger-ui/swagger-ui-standalone-preset.js",
            "/v3/api-docs",
            "/swagger-resources",
            "/swagger-resources/",
            "/swagger-resources/configuration/ui",
            "/swagger-resources/configuration/security",
            "/webjars/",
            "/favicon.ico"
    );


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        System.out.println("👉 Incoming path: " + path);

        // Пропускаем публичные пути И preflight OPTIONS
        if ("OPTIONS".equalsIgnoreCase(request.getMethod()) || isPublicPath(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        String key = request.getHeader("X-API-KEY");
        if (key != null && key.equals(validKey)) {
            filterChain.doFilter(request, response);
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    private boolean isPublicPath(String path) {
        return PUBLIC_PATHS.stream().anyMatch(path::startsWith);
    }
}


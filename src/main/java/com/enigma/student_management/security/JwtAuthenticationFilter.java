package com.enigma.student_management.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
           @NonNull HttpServletRequest request,
           @NonNull HttpServletResponse response,
           @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        // Step 1 Extract Authorization header
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        // Step 2. Check header format
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            // No token = skip filter, let security decide
            filterChain.doFilter(request, response);
            return;
        }

        // Step 3 Extract token from header
        jwt = authHeader.substring(7);

        // Step 4. Extract username from token
        username = jwtUtil.extractUsername(jwt);

        // Step 5. Validate and set authentication
        // SecurityContextHolder.getContext().getAuthentication() == null
        // means in this request user hasn't authentication yet
        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
            // Loads user from database
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // Validate token
            if(jwtUtil.validateToken(jwt, userDetails)){
                //Create authentication object
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                // Set request details
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Set authentication token ke Security Context
                // Setelah ini, Spring Security tau user ini sudah authenticated
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        // Step 6. Continue filter chain\
        filterChain.doFilter(request, response);
    }
}

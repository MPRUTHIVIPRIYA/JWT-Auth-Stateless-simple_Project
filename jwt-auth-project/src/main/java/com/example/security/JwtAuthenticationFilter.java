package com.example.security;

import com.example.service.UserService; 
import org.springframework.stereotype.Component;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse; 

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource; // FIX: Added missing import
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component // Makes this class a Spring bean
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserService userService;

    // Spring injects JwtService and UserService here
    public JwtAuthenticationFilter(JwtService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                    HttpServletResponse response, 
                                    FilterChain filterChain) throws ServletException, IOException {
        
        // 1. Check if the request has a proper Authorization header
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response); // Pass to the next filter if no token
            return;
        }

        jwt = authHeader.substring(7); // Extract the token (after "Bearer ")
        
        try {
            username = jwtService.extractUsername(jwt);
            
            // 2. Check token validity and if the user is already authenticated
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                
                // Fetch user details from database (via UserService)
                UserDetails userDetails = this.userService.loadUserByUsername(username);

                // 3. Final validation
                if (jwtService.validateToken(jwt, userDetails)) {
                    
                    // Create an authentication object for Spring Security
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities()
                    );
                    // This line now correctly resolves the class thanks to the new import:
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    
                    // Set the user as authenticated in the Spring Security context!
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            // Log the exception (e.g., token expired, signature invalid)
            System.err.println("JWT Validation Error: " + e.getMessage());
            // Important: We still call doFilter to let the rest of the chain (and Spring Security) handle the 403/401 response
        }

        filterChain.doFilter(request, response); // Continue the filter chain
    }
}

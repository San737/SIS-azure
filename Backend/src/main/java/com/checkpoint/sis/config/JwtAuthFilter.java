package com.checkpoint.sis.config;

import com.checkpoint.sis.service.impl.UserDetailsServiceImpl;
import com.checkpoint.sis.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // 1. Check if the Authorization header is present and correct
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response); // If not, pass the request to the next filter
            return;
        }

        // 2. Extract the JWT from the header
        jwt = authHeader.substring(7);

        // 3. Extract the user's email from the JWT
        userEmail = jwtUtil.extractUsername(jwt);

        // 4. Check if the user is not already authenticated
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // 5. Load the user's details from the database
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
            System.out.println("DEBUG: User Email: " + userDetails.getUsername());
            System.out.println("DEBUG: Authorities Loaded: " + userDetails.getAuthorities());

            // 6. Validate the token
            if (jwtUtil.validateToken(jwt, userDetails)) {
                // 7. If valid, set the user in the Spring Security context
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
            else {
                System.out.println("DEBUG: Token Validation Failed!");
            }
        }
        // 8. Pass the request to the next filter in the chain
        filterChain.doFilter(request, response);
    }
}

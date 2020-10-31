package com.akn.taskmanager.config;

import com.akn.taskmanager.service.JWTTokenService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    private JWTTokenService jwtTokenService;

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager, JWTTokenService jwtTokenService) {
        super(authenticationManager);
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String[] urls = {"/token/refresh", "/register" };
        boolean shouldNotFilter = Arrays.stream(urls)
                .anyMatch(url -> request.getRequestURI().contains(url));
        return shouldNotFilter;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader(jwtTokenService.getHeaderString());


        if (header == null || !header.startsWith(jwtTokenService.getTokenPrefix())) {
            chain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(jwtTokenService.getHeaderString());
        if (token != null) {
            // parse the token.
            String user = jwtTokenService.validateAccessToken(token);

            if (user != null) {
                return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
            }
            return null;
        }
        return null;
    }
}

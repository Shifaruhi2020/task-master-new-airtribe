package org.airtribe.TaskMaster.configuration;

import java.io.IOException;

import org.airtribe.TaskMaster.service.LogoutService;
import org.airtribe.TaskMaster.util.JwtUtil;
import org.springframework.web.filter.OncePerRequestFilter;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtFilter extends OncePerRequestFilter{

  private final LogoutService logoutService;

    public JwtFilter(LogoutService logoutService) {
        this.logoutService = logoutService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request,HttpServletResponse response, FilterChain filterChain) 
    throws ServletException, IOException

    {
      String authenticationHeader = request.getHeader("Authorization");

    // allowing unauthenticated access to public endpoints 
    if (request.getRequestURI().contains("/register") ||
        request.getRequestURI().contains("/verifyRegistration") ||
        request.getRequestURI().contains("/signin")) {
        filterChain.doFilter(request, response);
      return;
    }

    if (authenticationHeader == null) {
     response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
     response.getWriter().write("Missing Authorization header");
     return;
    }

    if (logoutService.isTokenBlacklisted(authenticationHeader)) {
          response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
          response.getWriter().write("Token has been blacklisted (logged out)");
          return;
  }

    if(!JwtUtil.validateJwtToken(authenticationHeader)) {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.getWriter().write("Invalid user token");
      return;
    }


    filterChain.doFilter(request, response);
  }
}

        

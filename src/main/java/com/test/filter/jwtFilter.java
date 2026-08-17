package com.test.filter;

import java.io.IOException;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.test.service.JWTService;
import com.test.service.MyUserDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class jwtFilter extends OncePerRequestFilter{

    @Autowired
    private JWTService jwtService;

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
       
                // getting the header 
                String authHeader = request.getHeader("Authorization");
                String token = null;
                String username = null;

                // checking do we have the authorization header
                if(authHeader != null && authHeader.startsWith("Bearer ")) {
                    token = authHeader.substring(7);
                    // here we will get the username from the token
                    username = jwtService.extractUserName(token);
                }

                // checking username is not null and its not authenticated
                if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                    // here we will validate the token and set the authentication in the security context
                  UserDetails userDetails =  applicationContext.getBean("myUserDetailsService", MyUserDetailsService.class).loadUserByUsername(username);

                  if(jwtService.validateToken(token, userDetails)) {
                    // here we will set the authentication in the security context
                    // we can create a username password authentication token and set it in the security context

                    UsernamePasswordAuthenticationToken authToken =  new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    
                  }
                
                }
                filterChain.doFilter(request, response);
    }

}

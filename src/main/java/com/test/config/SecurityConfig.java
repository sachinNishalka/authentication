package com.test.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import java.lang.reflect.Method;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.test.service.MyUserDetailsService;
import com.test.filter.jwtFilter;
import com.test.model.Permissions;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.security.config.Customizer;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private jwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {
        // here the configurations for the http security object will happen

        // disabling the csrf token, this will affect the post put patch requests, if
        // enabled
        http.csrf(customizer -> customizer.disable());

        // saying that every request should be authenticated
        http.authorizeHttpRequests(request -> request
                .requestMatchers("/user/register", "/user/login", "/token/validate", "/hello")
                .permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/user/**").hasRole("USER")
                .requestMatchers(HttpMethod.POST, "/admin/**").hasAuthority(Permissions.CREATE.name())
                .// here we can specify the endpoints that we want to permit without
                 // authentication
                anyRequest()
                .authenticated());

        // showing the basic username and password login form for browser requests
        http.formLogin(Customizer.withDefaults());

        // enabling basic username password authentication for http postman requests
        http.httpBasic(Customizer.withDefaults());

        // making the edpoints and requests stateless, so this will make the endpoint
        // stateless and cannot use in the browser
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        // there are multiple authentication providers, we can use the dao
        // authentication provider to check the users from the database
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        // we are not using any password encoders for now
        // provider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());
        // here use the instance of bcrypt password encoder and use the same strength
        provider.setPasswordEncoder(new BCryptPasswordEncoder(12));
        // // here we need a custom user detaisl service, to verify the users
        // provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}

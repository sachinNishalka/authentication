package com.test.service;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.test.model.UserEntity;

public class UserPrincipal implements UserDetails {

    // in order to use the following methods we need a user
    // we create a constuctor and foward the user here

    private UserEntity user;

    public UserPrincipal(UserEntity user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Set<SimpleGrantedAuthority> authorities = new HashSet<>();

        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
        authorities.addAll(user.getRole().getPermissions().stream()
                .map(permissions -> new SimpleGrantedAuthority(permissions.name())).collect(Collectors.toSet()));

        // here returning collection of authorities
        return authorities;

    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

}
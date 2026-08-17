package com.test.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.test.model.UserEntity;
import com.test.repository.UserRepository;

@Service
public class MyUserDetailsService implements UserDetailsService {

    // this should be connected with the user repo
    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      
        UserEntity user = userRepository.findByUsername(username);

        if(user == null) {
            System.out.println("User not found with username: " + username);
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        // if the user is found we can return an object of user details 
        // create a own class implements from the user details service, calls user principal


        return new UserPrincipal(user);
    }
    
}
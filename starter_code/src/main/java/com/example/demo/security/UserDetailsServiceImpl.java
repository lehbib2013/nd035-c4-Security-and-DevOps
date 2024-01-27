package com.example.demo.security;


import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.UserRepository;

import javax.persistence.EntityNotFoundException;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private static final Logger logger= LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Autowired
    private UserRepository userRepository;
    // Method to generate a Salt

    // Method to generate the hash.
//It takes a password and the Salt as input arguments

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            logger.info("Exception: user not found exception");
           // throw new UsernameNotFoundException(username);
            throw new EntityNotFoundException(String.format("User with name: %s not found", username));
        }
      //   String securePassword = get_SecurePassword(user.getPassword(), user.getSalt());
        return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword() , Collections.emptyList());
    }
}
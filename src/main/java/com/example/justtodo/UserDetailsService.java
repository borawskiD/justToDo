package com.example.justtodo;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    private final UserService userService;
    public UserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userService.findCredentialsByEmail(username).map(this::createUserDetails).orElseThrow(() -> new UsernameNotFoundException(String.format("User %s doesn't exist", username)));
    }

    private UserDetails createUserDetails(UserCredentialsDTO credentialsDTO){
        return User.builder().username(credentialsDTO.getEmail()).password(credentialsDTO.getPassword()).build();
    }
}

package com.example.justtodo.UserAuthorization;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    public final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public Optional<UserCredentialsDTO> findCredentialsByEmail(String email){
        System.out.println(userRepository.findByEmail(email).get().getEmail() + userRepository.findByEmail(email).get().getPassword());
        return userRepository.findByEmail(email).map(UserCredentialsDtoMapper::map);
    }

}

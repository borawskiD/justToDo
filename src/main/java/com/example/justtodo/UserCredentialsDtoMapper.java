package com.example.justtodo;
public class UserCredentialsDtoMapper {
    static UserCredentialsDTO map(User user){
        String email = user.getEmail();
        String password = user.getPassword();
        return new UserCredentialsDTO(email,password);
    }
}

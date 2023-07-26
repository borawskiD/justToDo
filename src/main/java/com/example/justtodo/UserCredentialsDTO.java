package com.example.justtodo;

public class UserCredentialsDTO {
    private final String email;
    private final String password;

    public UserCredentialsDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}

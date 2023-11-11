package com.example.justtodo.Api;

public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(Long id) {
        super("Could not find task: " + id);
    }
}

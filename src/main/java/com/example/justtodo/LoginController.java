package com.example.justtodo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    @GetMapping("/login")
    public String home(){
        System.out.println("hehe");
        return "loginForm.html";
    }
    @GetMapping("/hello")
    public String hello(){
        return "hello";
    }
}

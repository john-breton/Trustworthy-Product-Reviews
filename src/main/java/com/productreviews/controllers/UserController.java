package com.productreviews.controllers;

import com.productreviews.models.User;
import com.productreviews.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {
    @Autowired
    UserRepository userRepository;

    @GetMapping("/registration")
    public String newRegistration(User user, Model model){
        return "registration";
    }

    @PostMapping("/login")
    public String registerNewUser() { return "login"; }

    @GetMapping("/cancelRegistration")
    public String cancelRegistration() { return "login"; }

}

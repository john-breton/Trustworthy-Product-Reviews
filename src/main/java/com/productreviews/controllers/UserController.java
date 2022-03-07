package com.productreviews.controllers;

import com.productreviews.models.User;
import com.productreviews.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {
    @Autowired
    UserRepository userRepository;

    @GetMapping("/registration")
    public String newRegistration(User user){
        return "registration";
    }

}

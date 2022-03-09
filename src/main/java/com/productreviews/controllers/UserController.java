package com.productreviews.controllers;

import com.productreviews.models.User;
import com.productreviews.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@CrossOrigin
@RequestMapping("/user")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(RegistrationController.class);

    @Autowired
    UserRepository userRepository;

    /**
     * A request to just 'user' will redirect to the products page
     * @return redirect to products
     */
    @GetMapping
    public String user(){
        return "redirect:/user/products";
    }

    @GetMapping("/products")
    public String user_products(Authentication authentication, Model model){
        String current_user = authentication.getName();
        User user = userRepository.findByUsername(current_user);
        if(user == null || !authentication.isAuthenticated()){
            log.error("user not found or not authenticated");
        }
        model.addAttribute("user", user);
        return "user-page";
    }

    @GetMapping("/people")
    public String user_following(Authentication authentication, Model model){
        String current_user = authentication.getName();
        User user = userRepository.findByUsername(current_user);
        if(user == null || !authentication.isAuthenticated()){
            log.error("user not found or not authenticated");
        }
        model.addAttribute("user", user);
        return "user-following";
    }

}

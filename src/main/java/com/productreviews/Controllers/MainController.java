package com.productreviews.Controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.stereotype.Controller;

@Controller
@CrossOrigin(origins = "")
public class MainController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/")
    public String root() {
        return "user-page";
    }

    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    }

    @GetMapping("/user")
    public String userIndex() {
        return "user/index";
    }
}

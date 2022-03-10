package com.productreviews.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * The ProductController is responsible for handling all entry-point
 * related mappings. This currently includes the initial login page
 * and redirection to the appropriate user page upon user authentication.
 */
@Controller
@CrossOrigin(origins = "")
public class MainController {

    @GetMapping("/")
    public String root() {
        return "user-page";
    }

    /**
     * A navigation bar for the entirety of the website
     *
     * @return The NavBar
     */
    @GetMapping("/nav.html")
    public String navBar() {
        return "nav";
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

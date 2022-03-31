package com.productreviews.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * The MainController is responsible for handling all entry-point
 * related mappings. This currently includes the initial login page
 * and redirection to the appropriate user page upon user authentication.
 */
@Controller
@CrossOrigin
public class MainController {

    /**
     * Gets the landing page for the site
     * i.e. the page landed on after login
     *
     * @return The landing page template
     */
    @GetMapping("/")
    public String root() {
        return "redirect:/home";
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

    /**
     * Gets the login page for the site
     *
     * @return The login page
     */
    @GetMapping("/login")
    public String login() {
        return "login";
    }

}

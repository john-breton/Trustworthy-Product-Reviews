package com.productreviews.controllers;

import javax.validation.Valid;

import com.productreviews.services.UserService;
import com.productreviews.models.User;
import com.productreviews.models.UserRegistrationDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


/**
 * The RegistrationController is responsible for handling all
 * registration related mappings. This currently includes
 * the creation of user accounts and handling the event where
 * an account with a given username already exists in the system.
 */
@Controller
@CrossOrigin
@RequestMapping("/registration")
public class RegistrationController {

    @Autowired
    private UserService userService;

    private static final Logger log = LoggerFactory.getLogger(RegistrationController.class);

    /**
     * Add the user to the model upon registration
     *
     * @return A new UserRegistrationDto used for secure registration into the system.
     */
    @ModelAttribute("user")
    public UserRegistrationDto userRegistrationDto() {
        return new UserRegistrationDto();
    }

    /**
     * Gets the registration page for the site
     *
     * @return The registration page
     */
    @GetMapping
    public String showRegistrationForm() {
        return "registration";
    }

    /**
     * Attempts to register the user into the system
     *
     * @param userDto The object used to capture the credentials of the user to be
     * @param result  The result of the registration
     * @return The registration page with a success message on successful user addition, the registration page with a notice if the username selected already exists within the system.
     */
    @PostMapping
    public String registerUserAccount(@ModelAttribute("user") @Valid UserRegistrationDto userDto,
                                      BindingResult result) {
        log.info("registering user, username = " + userDto.getUsername() + ", password = " + userDto.getPassword());
        User existing = userService.findByUsername(userDto.getUsername());

        if (existing != null) {
            result.rejectValue("username", null, "There is already an account registered with that username");
        }

        if (result.hasErrors()) {
            log.info("BindingResult errors: " + result.getAllErrors());
            return "registration";
        }

        userService.save(userDto);
        return "redirect:/registration?success";
    }
}

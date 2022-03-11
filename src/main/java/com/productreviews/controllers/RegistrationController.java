package com.productreviews.controllers;

import javax.validation.Valid;

import com.productreviews.services.UserService;
import com.productreviews.models.User;
import com.productreviews.models.UserRegistrationDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    @ModelAttribute("user")
    public UserRegistrationDto userRegistrationDto() {
        return new UserRegistrationDto();
    }

    @GetMapping
    public String showRegistrationForm(Model model) {
        return "registration";
    }

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

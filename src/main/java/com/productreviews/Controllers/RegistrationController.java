package com.productreviews.Controllers;

import javax.validation.Valid;

import com.productreviews.Services.UserService;
import com.productreviews.Models.User;
import com.productreviews.Models.UserRegistrationDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


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
        log.info("registering user, user = " + userDto.getUsername() + " " + userDto.getPassword());
        User existing = userService.findByUsername(userDto.getUsername());

        if (existing != null) {
            result.rejectValue("username", null, "There is already an account registered with that email");
        }

        if (result.hasErrors()) {
            log.info("BindingResult errors: " + result.getAllErrors());
            return "registration";
        }

        userService.save(userDto);
        return "redirect:/registration?success";
    }
}

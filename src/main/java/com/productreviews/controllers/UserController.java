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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@CrossOrigin
@RequestMapping("/user")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(RegistrationController.class);

    @Autowired
    UserRepository userRepository;

    /**
     * A request to just 'user' will redirect to the products page
     *
     * @return redirect to products
     */
    @GetMapping
    public String user() {
        return "redirect:/user/products";
    }

    @GetMapping("/products")
    public String user_products(Authentication authentication, Model model) {
        String current_user = authentication.getName();
        User user = userRepository.findByUsername(current_user);
        if (user == null || !authentication.isAuthenticated()) {
            log.error("user not found or not authenticated");
        }
        model.addAttribute("user", user);
        return "user-products";
    }

    @GetMapping("/people")
    public String user_following(Authentication authentication, Model model) {
        String current_user = authentication.getName();
        User user = userRepository.findByUsername(current_user);
        if (user == null || !authentication.isAuthenticated()) {
            log.error("user not found or not authenticated");
        }
        model.addAttribute("user", user);

        // Order the users by Jaccard distance to avoid using JS at all costs
        Map<User, Double> usersAndJaccard = new HashMap<>();
        for (User currUser : Objects.requireNonNull(user).getFollowingList()) {
            usersAndJaccard.put(currUser, Objects.requireNonNull(user).getJaccardDistanceReviews(currUser));
        }

        // Magic
        Map<User, Double> sorted = usersAndJaccard.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        model.addAttribute("followers", sorted.keySet());
        return "user-following";
    }

    @GetMapping("/{username}")
    public String other_user(@PathVariable String username, Authentication authentication, Model model){
        User user = userRepository.findByUsername(username);
        if (user == null){
            System.out.println("Could not find user");
            return "error-page";
        }

        // Get current user
        String current_user = authentication.getName();
        User curr_user = userRepository.findByUsername(current_user);
        if (curr_user == null || !authentication.isAuthenticated()) {
            log.error("user not found or not authenticated");
        }

        Double jacc_distance = curr_user.getJaccardDistanceReviews(user);

        // Order the users by Jaccard distance to avoid using JS at all costs
        Map<User, Double> usersAndJaccard = new HashMap<>();
        for (User currUser : Objects.requireNonNull(user).getFollowingList()) {
            usersAndJaccard.put(currUser, Objects.requireNonNull(user).getJaccardDistanceReviews(currUser));
        }

        // Magic
        Map<User, Double> sorted = usersAndJaccard.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        model.addAttribute("jacc_distance", jacc_distance);
        model.addAttribute("followers", sorted.keySet());
        model.addAttribute("user", user);
        model.addAttribute("curr_user", curr_user);
        return "user-page";
    }

}

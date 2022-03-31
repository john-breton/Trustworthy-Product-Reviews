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

/**
 * The UserController is responsible for handling all
 * user related mappings. This currently includes
 * the users stuff which covers their followers and reviews.
 */
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
     * @return Redirect to products
     */
    @GetMapping
    public String user(Authentication authentication) {
        // Get current user
        String current_user = authentication.getName();
        User curr_user = userRepository.findByUsername(current_user);
        return "redirect:/user/" + curr_user.getUsername();
    }

    /**
     * Gets the page when a user visits their user page
     *
     * @param username The username of the user page we will return
     * @param authentication The authentication status of the current user
     * @param model The model we will be adding the user page details to
     * @return The user-page if the user is authenticated, error-page otherwise
     */
    @GetMapping("/{username}")
    public String otherUser(@PathVariable String username, Authentication authentication, Model model) {
        User user = userRepository.findByUsername(username);

        // Get current user
        String currentUser = authentication.getName();
        User currUser = userRepository.findByUsername(currentUser);
        if (user == null || currUser == null || !authentication.isAuthenticated()) {
            log.error("user not found or not authenticated");
            return "error-page";
        }

        Double jaccDistance = currUser.getJaccardDistanceReviews(user);

        // Order the users by Jaccard distance to avoid using JS at all costs
        Map<User, Double> sorted = getJaccardMap(user);

        model.addAttribute("jacc_distance", jaccDistance);
        model.addAttribute("followers", sorted.keySet());
        model.addAttribute("user", user);
        model.addAttribute("curr_user", currUser);
        return "user-page";
    }

    /**
     * Gets the page when a user tries to go to another user's profile
     *
     * @param username The username that the current user is trying to view
     * @param authentication The authentication status of the current user
     * @return The user-page of the target user if the current user is authenticated, error-page otherwise
     */
    @GetMapping("/follow/{username}")
    public String follow(@PathVariable String username, Authentication authentication) {
        String currentUser = authentication.getName();
        User user = userRepository.findByUsername(currentUser);
        if (user == null || !authentication.isAuthenticated()) {
            log.error("User not found or not authenticated");
            return "error-page";
        }
        User followedUser = userRepository.findByUsername(username);
        Objects.requireNonNull(user).addFollowing(followedUser);
        userRepository.save(user);
        return "redirect:/user/" + followedUser.getUsername();
    }

    /**
     * Order all users in the system by the Jaccard distance from a target user.
     *
     * @param user The target user for which all other users will be ordered based on their Jaccard distance.
     * @return A Map of Users and their Jaccard distance from the target User.
     */
    private Map<User, Double> getJaccardMap(User user) {
        // Order the users by Jaccard distance to avoid using JS at all costs
        Map<User, Double> usersAndJaccard = new HashMap<>();
        for (User currUser : Objects.requireNonNull(user).getFollowingList()) {
            usersAndJaccard.put(currUser, Objects.requireNonNull(user).getJaccardDistanceReviews(currUser));
        }

        // Magic
        return usersAndJaccard.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }
}

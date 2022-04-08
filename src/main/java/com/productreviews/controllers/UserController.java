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
     * Gets the page when a user visits another user page
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
     * handles clicking on the 'follow' button in the user page.
     * @param username The username of the person to follow
     * @param redirectUsername The username of the person who's page we're on
     * @param authentication The authentication status of the current user
     * @return The same user page that called this method, or an error page if request was invalid
     */
    @GetMapping("/follow/{username}/{redirectUsername}")
    public String follow(@PathVariable String username, @PathVariable(required = false) String redirectUsername,
                         Authentication authentication){
        String currentUser = authentication.getName();
        User user = userRepository.findByUsername(currentUser);

        if (user == null || !authentication.isAuthenticated()) {
            log.error("User not found or not authenticated");
            return "error-page";
        }

        if (redirectUsername == null){
            redirectUsername = user.getUsername();
        }

        User followedUser = userRepository.findByUsername(username);
        Objects.requireNonNull(user).addFollowing(followedUser);
        userRepository.save(user);
        return "redirect:/user/" + redirectUsername;
    }

    /**
     * Unfollow a user from the following page
     * @param authentication of the current logged in user
     * @param model of the app
     * @return the page that confirms the unfollowing of the user
     */
    @GetMapping("/unfollowUser/{username}/{redirectUsername}")
    public String unfollowUser(@PathVariable String username, @PathVariable(required = false) String redirectUsername,
                               Authentication authentication, Model model) {


        User unfollowed = userRepository.findByUsername(username);
        String currentUser = authentication.getName();
        User user = userRepository.findByUsername(currentUser);

        if (unfollowed == null || user == null || !authentication.isAuthenticated()) {
            log.error("User not found or not authenticated");
            return "error-page";
        }

        if (redirectUsername == null){
            redirectUsername = unfollowed.getUsername();
        }

        user.unFollow(unfollowed);
        userRepository.save(user);
        model.addAttribute("user", unfollowed);
        return "redirect:/user/" + redirectUsername;

    }

    /**
     * Unfollow a user from the product page
     * @param userId of the user to be unfollowed
     * @param authentication of the current logged in user
     * @param model of the app
     * @return the page that confirms the unfollowing of the user
     */
    @GetMapping("/unfollow/{userId}/{productId}")
    public String unfollowUserProduct(@PathVariable Long userId,@PathVariable Long productId, Authentication authentication,
                                      Model model) {
        User unfollowed = userRepository.findById(userId).orElse(null);
        String currentUser = authentication.getName();
        User user = userRepository.findByUsername(currentUser);
        if (user == null || !authentication.isAuthenticated()) {
            log.error("User not found or not authenticated");
        }
        user.unFollow(unfollowed);
        userRepository.save(user);
        model.addAttribute("user", unfollowed);
        model.addAttribute("productid", productId);
        return "redirect:/product/" + productId;
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

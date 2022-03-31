package com.productreviews.controllers;

import com.productreviews.models.Product;
import com.productreviews.models.User;
import com.productreviews.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
    public String user(Authentication authentication) {
        // Get current user
        String current_user = authentication.getName();
        User curr_user = userRepository.findByUsername(current_user);
        return "redirect:/user/" + curr_user.getUsername();
    }

    @GetMapping("/{username}")
    public String other_user(@PathVariable String username, Authentication authentication, Model model){
        User user = userRepository.findByUsername(username);

        // Get current user
        String current_user = authentication.getName();
        User curr_user = userRepository.findByUsername(current_user);
        if (user==null || curr_user == null || !authentication.isAuthenticated()) {
            log.error("user not found or not authenticated");
            return "error-page";
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

    @GetMapping("/follow/{username}")
    public String follow(@PathVariable String username, Authentication authentication, Model model){
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
     * Unfollow a user from the following page
     * @param authentication of the current logged in user
     * @param model of the app
     * @return the page that confirms the unfollowing of the user
     */
    @GetMapping("/unfollowUser/{username}/{redirect_username}")
    public String unfollowUser(@PathVariable String username, @PathVariable(required = false) String redirect_username,
                               Authentication authentication, Model model) {


        User unfollowed = userRepository.findByUsername(username);
        String currentUser = authentication.getName();
        User user = userRepository.findByUsername(currentUser);

        if (unfollowed == null || user == null || !authentication.isAuthenticated()) {
            log.error("User not found or not authenticated");
            return "error-page";
        }

        User redirect_user = user;
        if (redirect_username != null){
            redirect_user = userRepository.findByUsername(redirect_username);
        }

        user.unFollow(unfollowed);
        userRepository.save(user);
        model.addAttribute("user", unfollowed);
        return "redirect:/user/" + redirect_user.getUsername();

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
        return "unfollow";
    }
}

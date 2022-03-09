package com.productreviews.controllers;

import com.productreviews.models.*;
import com.productreviews.repositories.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

/**
 * The ProductController is responsible for handling all Product
 * related mappings. This currently includes creation of products,
 * viewing of products, display of reviews for a product, and
 * supporting following other users via a product review.
 */
@Controller
public class ProductController {

    private static final Logger log = LoggerFactory.getLogger(RegistrationController.class);

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    /**
     * REMOVE LATER
     * Creates a product for the system that can be reviewed by users.
     *
     * @param productId The unique ID number of the product being created
     * @param model     The model which the changes will be rendered on
     * @return On successful product creation, the createProduct page
     */
    @GetMapping("/create/{productId}")
    public String createProduct(@PathVariable int productId, Authentication authentication, Model model) {
        Product product = new Product("ProductName", "product1.jpg", (long) productId);
        productRepository.save(product);
        return "createProduct";
    }

    /**
     * View the product page for a product, should it exist. This product
     * page includes the average rating of the product, alongside its name,
     * image, description, and any reviews that have been associated with it.
     *
     * @param productId The ID number of the product to be displayed
     * @param model     The model which the changes will be rendered on
     * @return On successful product retrieval, the product page
     */
    @GetMapping("/product/{productId}")
    public String viewProductPage(@PathVariable int productId, Authentication authentication, Model model) {
        Product product = productRepository.findById((long) productId).orElse(null);
        String currentUser = authentication.getName();
        User user = userRepository.findByUsername(currentUser);
        if (user == null || !authentication.isAuthenticated()) {
            log.error("User not found or not authenticated");
        }
        model.addAttribute("user", user);
        model.addAttribute("product", product);
        model.addAttribute("reviews", Objects.requireNonNull(product).getReviews());
        model.addAttribute("newReview", new Review());
        return "product";
    }


    /**
     * View the review page for a product after a review for a product is
     * submitted. This page provides confirmation to the user that a review
     * was submitted while allowing for them to return to the product page
     * after acknowledging the confirmation
     *
     * @param productId The ID number of the product for which the review is being added to
     * @param model     The model which the changes will be rendered on
     * @return On successful review creation, the review page
     */
    @GetMapping("/review/{productId}")
    public String viewReviewPage(@PathVariable int productId, @RequestParam(value = "score") int score, @RequestParam(value = "content") String content, Authentication authentication, Model model) {
        Product product = productRepository.findById((long) productId).orElse(null);
        String currentUser = authentication.getName();
        User user = userRepository.findByUsername(currentUser);
        if (user == null || !authentication.isAuthenticated()) {
            log.error("User not found or not authenticated");
        }
        // TODO Map this to an error page rather than crash the entire program :')
        if (reviewRepository.findByUser(user).stream().anyMatch(review -> review.getAssociatedProduct().equals(productRepository.findById((long) productId).orElse(null)))) {
            log.error("User has already created a review for this product.");
        } else {
            Review review = new Review();
            review.setScore(score);
            review.setContent(content);
            review.setUser(user);
            review.setAssociatedProduct(product);
            reviewRepository.save(review);
            Objects.requireNonNull(user).addReview(review);
            Objects.requireNonNull(product).addReview(review);
            model.addAttribute("product", product);
            model.addAttribute("review", review);
        }
        return "review";
    }

    /**
     * SHOULD MAP TO THE USER PAGE
     * View the user page of a user that has written a review for a product. This
     * is meant to occur after a user follows another user.
     *
     * @param productId The ID number of the product for which the user followed this user from
     * @param username  The username of the user that reviewed the product
     * @param model     The model which the changes will be rendered on
     * @return On successful follow, the follow-page
     */
    @GetMapping("/follow/{productId}/{username}")
    public String viewUserPage(@PathVariable int productId, @PathVariable String username, Authentication authentication, Model model) {
        Product product = productRepository.findById((long) productId).orElse(null);
        String currentUser = authentication.getName();
        User user = userRepository.findByUsername(currentUser);
        if (user == null || !authentication.isAuthenticated()) {
            log.error("User not found or not authenticated");
        }
        User followedUser = userRepository.findByUsername(username);
        Objects.requireNonNull(user).addFollowing(followedUser);
        model.addAttribute("product", product);
        model.addAttribute("author", username);
        return "follow-page";
    }
}

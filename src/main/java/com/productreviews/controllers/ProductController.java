package com.productreviews.controllers;

import com.productreviews.models.*;
import com.productreviews.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    /**
     * REMOVE LATER
     * @param product_id
     * @param model
     * @return
     */
    @GetMapping("/create/{product_id}")
    public String createProduct(@PathVariable int product_id, Authentication authentication, Model model){
        Product product = new Product("ProductName","product1.jpg", new Long (product_id));
        productRepository.save(product);
        return "createProduct";
    }

    /**
     * @param product_id
     * @param model
     * @return
     */
    @GetMapping("/product/{product_id}")
    public String viewProductPage(@PathVariable int product_id, Authentication authentication, Model model){
        Product product  = productRepository.findById(new Long(product_id)).orElse(null);
        String current_user=authentication.getName();
        User user = userRepository.findByUsername(current_user);
        if(user == null || !authentication.isAuthenticated()){
            //log.error("user not found or not authenticated");
        }
        model.addAttribute("user", user);
        model.addAttribute("product", product);
        model.addAttribute("reviews", product.getReviews());
        model.addAttribute("newReview", new Review());
        return "product";
    }


    /**
     * @param product_id
     * @param model
     * @return
     */
    @GetMapping("/review/{product_id}")
    public String viewReviewPage(@PathVariable int product_id, @RequestParam(value = "score") int score, @RequestParam(value = "content") String content,Authentication authentication, Model model){
        Product product  = productRepository.findById(new Long(product_id)).orElse(null);
        String current_user=authentication.getName();
        User user = userRepository.findByUsername(current_user);
        if(user == null || !authentication.isAuthenticated()){
            //log.error("user not found or not authenticated");
        }
        //should a user be able to write as many reviews as they want? or limit it to one review per user?
        Review review  = new Review();
        review.setAuthor(current_user);
        review.setScore(score);
        review.setContent(content);
        review.setUser(user);
        review.setAssociatedProduct(product);
        reviewRepository.save(review);
        user.addReview(review);
        product.addReview(review);
        model.addAttribute("product", product);
        model.addAttribute("review", review);
        return "review";
    }

    /**
     *  SHOULD MAP TO THE USER PAGE
     * @param author
     * @param model
     * @return
     */
    @GetMapping("/follow/{product_id}/{author}")
    public String viewUserPage(@PathVariable int product_id, @PathVariable String author,Authentication authentication, Model model){
        Product product  = productRepository.findById(new Long(product_id)).orElse(null);
        String current_user=authentication.getName();
        User user = userRepository.findByUsername(current_user);
        if(user == null || !authentication.isAuthenticated()){
            //log.error("user not found or not authenticated");
        }
        User followedUser= userRepository.findByUsername(author);
        user.addFollowing(followedUser);
        model.addAttribute("product", product);
        model.addAttribute("author", author);
        return "follow-page";
    }
}

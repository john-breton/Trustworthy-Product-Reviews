package com.productreviews.controllers;

import com.productreviews.models.*;
import com.productreviews.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
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
     * JUST INCLUDED FOR CURRENT USE, SHOULD REMOVE WHEN INTEGRATED WITH THE FULL LOGIC
     *  create a product with the specified id and user with the id specified
     * @param product_id
     * @param model
     * @return
     */
    @GetMapping("/create/{user_id}/{product_id}")
    public String createProduct(@PathVariable int user_id, @PathVariable int product_id, Model model){
        Product product = new Product("ProductName","product1.jpg", new Long (product_id));
        User user = new User("user", new Long(user_id));
        productRepository.save(product);
        userRepository.save(user);
        return "createProduct";
    }

    @GetMapping("/{user_id}/product/{product_id}")
    public String viewProductPage(@PathVariable int user_id, @PathVariable int product_id, Model model){
        Product product  = productRepository.findById(new Long(product_id)).orElse(null);
        User user  = userRepository.findById(new Long(user_id)).orElse(null);
        model.addAttribute("user_id", user.getID());
        model.addAttribute("product", product);
        model.addAttribute("reviews", product.getReviews());
        model.addAttribute("newReview", new Review());
        return "product";
    }


    @GetMapping("/{user_id}/review/{product_id}")
    public String viewReviewPage(@PathVariable int user_id, @PathVariable int product_id, @RequestParam(value = "rating") int rating, @RequestParam(value = "comment") String comment, Model model){
        Product product  = productRepository.findById(new Long(product_id)).orElse(null);
        User user  = userRepository.findById(new Long(user_id)).orElse(null);
        Review review  = new Review(product_id+ user.getID());
        review.setRating(rating);
        review.setComment(comment);
        review.setUser(user);
        review.setProduct(product);
        reviewRepository.save(review);
        user.addReview(review);
        product.addReview(review);
        model.addAttribute("product", product);
        model.addAttribute("user_id", user.getID());
        model.addAttribute("review", review);
        return "review";
    }

    /**
     *  SHOULD MAP TO THE USER PAGE
     * @param user_id
     * @param model
     * @return
     */
    @GetMapping("/follow/{user_id}")
    public String viewUserPage(@PathVariable int user_id, Model model){
        return "result";
    }
}

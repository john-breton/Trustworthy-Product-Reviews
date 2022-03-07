package com.productreviews.Controllers;

import com.productreviews.Models.*;
import com.productreviews.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@Controller
public class mockProductController {

    @Autowired
    private mockProductRepository productRepository;

    @Autowired
    private mockUserRepository userRepository;

    @Autowired
    private mockReviewRepository reviewRepository;

    /**
     * create a product with the specified id
     * @param product_id
     * @param model
     * @return
     */
    @GetMapping("/create/{product_id}")
    public String createProduct(@PathVariable int product_id, Model model){
        mockProduct product = new mockProduct("ProductName","product1.jpg", new Long (product_id));
        mockUser user = new mockUser("user", new Long(3));
        productRepository.save(product);
        userRepository.save(user);
        return "createProduct";
    }

    @GetMapping("/product/{product_id}")
    public String viewProductPage(@PathVariable int product_id, Model model){
        mockProduct product  = productRepository.findById(new Long(product_id)).orElse(null);
        mockUser user  = userRepository.findById(new Long(3)).orElse(null);
        model.addAttribute("product", product);
        model.addAttribute("reviews", product.getReviews());
        model.addAttribute("newReview", new mockReview());
        return "product";
    }


    @GetMapping("/review/{product_id}")
    public String viewReviewPage(@PathVariable int product_id, @RequestParam(value = "rating") int rating, @RequestParam(value = "comment") String comment, Model model){
        mockProduct product  = productRepository.findById(new Long(product_id)).orElse(null);
        mockUser user  = userRepository.findById(new Long(3)).orElse(null);
        mockReview review  = new mockReview(product_id+ user.getID());
        review.setRating(rating);
        review.setComment(comment);
        review.setUser(user);
        review.setProduct(product);
        reviewRepository.save(review);
        user.addReview(review);
        product.addReview(review);
        model.addAttribute("product_id", product.getId());
        model.addAttribute("review", review);
        return "review";
    }

    @GetMapping("/follow/{user_id}")
    public String viewUserPage(@PathVariable int user_id, Model model){
        return "result";
    }
}

package com.productreviews.controllers;

import java.util.List;
import java.util.Objects;

import com.productreviews.models.Product;
import com.productreviews.models.Review;
import com.productreviews.models.User;
import com.productreviews.models.common.Category;
import com.productreviews.repositories.ProductRepository;
import com.productreviews.repositories.ReviewRepository;
import com.productreviews.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

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
    @GetMapping("/create/{productId}/{productName}/{category}")
    public String createProduct(@PathVariable int productId, @PathVariable String productName,
                                @PathVariable String category, Authentication authentication, Model model) {

        if (category.equals("book")) {
            Product product = new Product(productName, "product1.jpg", Category.BOOK, (long) productId);
            productRepository.save(product);
        } else {
            Product product = new Product(productName, "product1.jpg", Category.NOT_BOOK, (long) productId);
            productRepository.save(product);
        }
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
     * View the user landing page. This page features a main list of all products in alphabetical order with their
     * name, image and average reviews.
     * It also features a sidebar with users that are similar to the user
     *
     * @param model The model which the changes will be rendered on
     * @return The Landing Page
     */
    @GetMapping("/home")
    public String viewLandingPage(Authentication authentication, Model model) {
        String currentUser = authentication.getName();
        User user = userRepository.findByUsername(currentUser);
        if (user == null || !authentication.isAuthenticated()) {
            log.error("User not found or not authenticated");
        }
        model.addAttribute("mainUser", user);
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("products", productRepository.findAll());
        return "landingPage";
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
    public String viewReviewPage(@PathVariable int productId, @RequestParam(value = "score") int score,
                                 @RequestParam(value = "content") String content, Authentication authentication,
                                 Model model) {
        Product product = productRepository.findById((long) productId).orElse(null);
        String currentUser = authentication.getName();
        User user = userRepository.findByUsername(currentUser);
        if (user == null || !authentication.isAuthenticated()) {
            log.error("User not found or not authenticated");
        }
        boolean reviewExists = false;
        Long reviewID = new Long(0);
        Review review;

        //check if the user already reviewed this product, then override with the new review
        for (int i = 0; i < product.getReviews().size(); i++) {
            if (user.hasReview(product.getReviews().get(i).getId())) {
                reviewID = product.getReviews().get(i).getId();
                reviewExists = true;
            }
        }
        if (reviewExists) {
            review = reviewRepository.findById(reviewID).orElse(null);
            review.setScore(score);
            review.setContent(content);
            reviewRepository.save(review);
        } else {
            review = new Review();
            review.setScore(score);
            review.setContent(content);
            review.setUser(user);
            review.setAssociatedProduct(product);
            reviewRepository.save(review);
            Objects.requireNonNull(user).addReview(review);
            Objects.requireNonNull(product).addReview(review);
        }
        product.updateAverageRating();
        productRepository.save(product);
        model.addAttribute("product", product);
        model.addAttribute("review", review);
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
    public String viewUserPage(@PathVariable int productId, @PathVariable String username,
                               Authentication authentication, Model model) {
        Product product = productRepository.findById((long) productId).orElse(null);
        String currentUser = authentication.getName();
        User user = userRepository.findByUsername(currentUser);
        if (user == null || !authentication.isAuthenticated()) {
            log.error("User not found or not authenticated");
        }
        User followedUser = userRepository.findByUsername(username);
        Objects.requireNonNull(user).addFollowing(followedUser);
        userRepository.save(user);
        model.addAttribute("product", product);
        model.addAttribute("author", username);
        return "follow-page";
    }

    /**
     * sort the products by averahe rating. Sorting could be by average rating low to high
     * or by average rating high to low
     *
     * @param model The model which the changes will be rendered on
     * @return On successful review creation, the review page
     */
    @GetMapping("/products/filterandsearch")
    public String viewReviewPage(@RequestParam(required = false) String sort,
                                 @RequestParam(required = false) String category, Authentication authentication,
                                 Model model) {
        String currentUser = authentication.getName();
        User user = userRepository.findByUsername(currentUser);
        if (user == null || !authentication.isAuthenticated()) {
            log.error("User not found or not authenticated");
        }
        if (sort == null || category == null) {
            log.error("Invalid Page Request");
            return "error-page";
        }
        model.addAttribute("mainUser", user);
        model.addAttribute("users", userRepository.findAll());
        if (sort.equals("asc") && category.equals("none")) {
            model.addAttribute("products", productRepository.findByOrderByAverageRatingAsc());
        } else if (sort.equals("desc") && category.equals("none")) {
            model.addAttribute("products", productRepository.findByOrderByAverageRatingDesc());
        } else if (category.equals("none") && sort.equals("none")) {
            return "redirect:/home";
        } else if (!category.equals("none") && sort.equals("none")) {
            Category categoryEnum = Category.valueOf(category);
            model.addAttribute("products", productRepository.findByCategory(categoryEnum));
        } else if (sort.equals("asc")) {
            Category categoryEnum = Category.valueOf(category);
            model.addAttribute("products", productRepository.findByCategoryOrderByAverageRatingAsc(categoryEnum));
        } else if (sort.equals("desc")) {
            Category categoryEnum = Category.valueOf(category);
            model.addAttribute("products", productRepository.findByCategoryOrderByAverageRatingDesc(categoryEnum));
        }
        return "landingPage";
    }

    /**
     * Filters reviews based on followage or the entire list of reviews. Also filters based on min and max rating
     *
     * @param productId the ID of the product to retreive the reviews for
     * @param userReviewFilter indicates whether the reviews should include all or only the users the the current user follows
     * @param minStarFilter indicates the minimum rating that the user wishes to see
     * @param maxStarFilter indicates the maximum rating that the user wished to see
     * @param authentication provides access to the current user object
     * @param model The model which the changes will be rendered on
     * @return On successful product page updated, error page on error
     */
    @GetMapping("/filterreviews/{productId}")
    public String viewFilteredReviews(@PathVariable int productId,
                                      @RequestParam(required = false) String userReviewFilter,
                                      @RequestParam(required = false) int minStarFilter,
                                      @RequestParam(required = false) int maxStarFilter,
                                      Authentication authentication, Model model) {
        Product product = productRepository.findById((long) productId).orElse(null);
        String currentUser = authentication.getName();
        User user = userRepository.findByUsername(currentUser);

        log.info("Filtering reviews! " + userReviewFilter + " Max :" + maxStarFilter + " Min: " + minStarFilter);

        if (user == null || !authentication.isAuthenticated()) {
            log.error("User not found or not authenticated");
            return "error-page";
        }
        if (product == null) {
            log.error("Product not found");
            return "error-page";
        }
        if (userReviewFilter == null || minStarFilter == -1 || maxStarFilter == -1) {
            log.error("Invalid Page Request");
            return "error-page";
        }
        model.addAttribute("user", user);
        model.addAttribute("product", product);
        model.addAttribute("newReview", new Review());

        if (userReviewFilter.equals("all")) {
            List<Review> reviews = reviewRepository.findAllByAssociatedProductIdAndScoreGreaterThanEqualAndScoreLessThanEqual(productId, minStarFilter,
                                                                                 maxStarFilter);
            model.addAttribute("reviews", reviews);
            product.setAverageRating(reviews.size() > 0 ? reviews.stream().mapToDouble(Review::getScore).sum()/reviews.size() : 0.0);
        } else if (userReviewFilter.equals("following")) {
            List<Review> reviews = reviewRepository.findAllByAssociatedProductIdAndUserInAndScoreGreaterThanEqualAndScoreLessThanEqual(productId, user.getFollowingList(), minStarFilter,
                                                                    maxStarFilter);
            model.addAttribute("reviews", reviews);
            product.setAverageRating(reviews.size() > 0 ? reviews.stream().mapToDouble(Review::getScore).sum()/reviews.size() : 0.0);
        }

        return "product";
    }

}

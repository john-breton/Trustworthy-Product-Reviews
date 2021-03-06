package com.productreviews.controllers;

import java.util.*;
import java.util.stream.Collectors;

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

        // Order the users by Jaccard distance
        Map<User, Double> usersAndJaccard = new HashMap<>();
        for (User currUser : userRepository.findAll()) {
            usersAndJaccard.put(currUser, Objects.requireNonNull(user).getJaccardDistanceReviews(currUser));
        }

        // Sort the keys of the entry set using their values and return the new sorting
        Map<User, Double> sorted = usersAndJaccard.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        model.addAttribute("users", sorted.keySet());
        model.addAttribute("products", productRepository.findAll());
        return "landing-page";
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
        long reviewID = 0L;
        Review review;

        //check if the user already reviewed this product, then override with the new review
        for (int i = 0; i < Objects.requireNonNull(product).getReviews().size(); i++) {
            if (Objects.requireNonNull(user).hasReview(product.getReviews().get(i).getId())) {
                reviewID = product.getReviews().get(i).getId();
                reviewExists = true;
                break;
            }
        }
        if (reviewExists) {
            review = reviewRepository.findById(reviewID).orElse(null);
            Objects.requireNonNull(review).setScore(score);
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
        return "redirect:/product/" + productId;
    }

    /**
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
        return "redirect:/product/" + productId;
    }

    /**
     * Sort the products by average rating. Sorting could be by average rating low to high
     * or by average rating high to low
     *
     * @param model The model which the changes will be rendered on
     * @return On successful review creation, the review page
     */
    @GetMapping("/products/filter")
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
        return "landing-page";
    }

    /**
     * Filters reviews based on followers or the entire list of reviews. Also filters based on min and max rating, and
     * their similarity scores using Jaccard distance
     *
     * @param productId        The ID of the product to retrieve the reviews for
     * @param userReviewFilter Indicates whether the reviews should include all or only the users the current user follows
     * @param jaccardFilter    Indicates whether the reviews should be ordered from High to Low Jaccard Distance or Low to High
     * @param minStarFilter    Indicates the minimum rating that the user wishes to see
     * @param maxStarFilter    Indicates the maximum rating that the user wished to see
     * @param authentication   Provides access to the current user object
     * @param model            The model which the changes will be rendered on
     * @return On success the updated product page, error page otherwise
     */
    @GetMapping("/filterreviews/{productId}")
    public String viewFilteredReviews(@PathVariable int productId,
                                      @RequestParam(required = false) String userReviewFilter,
                                      @RequestParam(required = false) String jaccardFilter,
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
            jaccardFiltering(jaccardFilter, model, product, user, reviews);
        } else if (userReviewFilter.equals("following")) {
            List<Review> reviews = reviewRepository.findAllByAssociatedProductIdAndUserInAndScoreGreaterThanEqualAndScoreLessThanEqual(productId, user.getFollowingList(), minStarFilter,
                    maxStarFilter);
            jaccardFiltering(jaccardFilter, model, product, user, reviews);
        }

        return "product";
    }

    /**
     * Filters reviews by Jaccard distance and returns them in order depending on the selected filter option.
     *
     * @param jaccardFilter Indicates whether the reviews should be ordered from High to Low Jaccard distance or Low to High
     * @param model         The model which the changes will be rendered on
     * @param product       The product object where the reviews are located
     * @param user          The current user logged into the system
     * @param reviews       A list of review objects that will be filtered by their relative Jaccard distances to the current user.
     */
    private void jaccardFiltering(@RequestParam(required = false) String jaccardFilter, Model model, Product product, User user, List<Review> reviews) {
        jaccardReviews(model, user, reviews);
        if (jaccardFilter.equals("HL")) {
            Collections.reverse(reviews);
        }

        model.addAttribute("reviews", reviews);
        product.setAverageRating(reviews.size() > 0 ? reviews.stream().mapToDouble(Review::getScore).sum() / reviews.size() : 0.0);
    }

    /**
     * Stream reviews and sort a map of reviews and Jaccard distances
     *
     * @param model   The model which the changes will be rendered on
     * @param user    The current user logged into the system
     * @param reviews A list of review objects that will be filtered by their relative Jaccard distances to the current user.
     */
    private void jaccardReviews(Model model, User user, List<Review> reviews) {
        Map<Review, Double> reviewsAndJaccard = new HashMap<>();
        for (Review review : reviews) {
            reviewsAndJaccard.put(review, user.getJaccardDistanceReviews(review.getUser()));
        }

        Map<Review, Double> sorted = reviewsAndJaccard.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        model.addAttribute("sortedset", sorted.keySet());
        reviews.clear();
        reviews.addAll(sorted.keySet());
    }

}

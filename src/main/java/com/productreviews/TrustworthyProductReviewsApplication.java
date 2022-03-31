package com.productreviews;

import com.github.javafaker.Faker;
import com.productreviews.models.Product;
import com.productreviews.models.Review;
import com.productreviews.models.User;
import com.productreviews.models.UserRegistrationDto;
import com.productreviews.repositories.ProductRepository;
import com.productreviews.repositories.ReviewRepository;
import com.productreviews.repositories.UserRepository;
import com.productreviews.services.UserServiceImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * Entry point for the application. For local development,
 * navigate to localhost:8080 upon launch.
 */
@SpringBootApplication
public class TrustworthyProductReviewsApplication {

    public static void main(String[] args) {
        SpringApplication.run(TrustworthyProductReviewsApplication.class, args);
    }

    /**
     * The number of users to pre-populate the system with upon initialization
     */
    public static final int numOfUsers = 10;

    /**
     * CommandLineRunner a special bean that lets you execute some logic
     * after the application context is loaded and started.
     * <p>
     * This one in particular is used to populate the database with dummy data
     *
     * @param userRepository    The crud repository where User objects will be stored
     * @param productRepository The crud repository where Product objects will be stored
     * @param reviewRepository  The crud repository where Review objects will be stored
     * @param userService       Authorization class that provides useful user lookup and saving services
     * @return The CommandLineRunner that is used to pre-populate the database
     */
    @Bean
    public CommandLineRunner populate(UserRepository userRepository, ProductRepository productRepository,
                                      ReviewRepository reviewRepository, UserServiceImpl userService) {
        return args -> {
            Faker faker = new Faker();

            // Create a user we can login with
            User user;
            ArrayList<User> users = new ArrayList<>();

            UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
            userRegistrationDto.setUsername("Team21");
            userRegistrationDto.setPassword("password");
            user = userService.save(userRegistrationDto);
            users.add(user);

            // Create the users
            for (int i = 0; i < numOfUsers; i++) {
                String name = faker.funnyName().name();

                //  if the name is longer than 20 characters, then splice it
                if (name.length() > 20) name = name.substring(0, 19);

                user = new User(name, "password");
                try {
                    userRepository.save(user);
                    users.add(user);
                } catch (Exception exception) {
                    System.out.println("exception saving user: \n" + exception);
                }

            }

            // Create the products
            ArrayList<Product> products = new ArrayList<>();
            try {
                File file = new File("products.txt");
                Scanner myReader = new Scanner(file);
                Product product;
                while (myReader.hasNextLine()) {
                    String data = myReader.nextLine();
                    product = Product.createProductFromString(data);
                    if (product != null) {
                        productRepository.save(product);
                        products.add(product);
                    }
                }
                myReader.close();
            } catch (FileNotFoundException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }


            // Create reviews for users and have them follow each other
            Random random = new Random();

            int numOfProducts = products.size();
            int numOfUsers = users.size();
            int numOfFollowing, numReviews, randomProductI, randomUserI, index = 0;

            ArrayList<Integer> alreadyReviewed, alreadyFollowed;

            ArrayList<Review> reviews = new ArrayList<>();

            Review review;

            for (User curr_user : users) {

                System.out.println("****** curr user is " + curr_user);
                // empty it for next user
                alreadyReviewed = new ArrayList<>();
                alreadyFollowed = new ArrayList<>();
                alreadyFollowed.add(index); // add curr user to already followed so that they dont try following themselves
                index++;

                // generate a random number to determine how many products they're gonna review
                numReviews = random.nextInt(numOfProducts);
                System.out.println("Gonna review " + numReviews + " products");
                Product product;

                // Create reviews for each user
                for (int i = 0; i < numReviews; i++) {
                    randomProductI = random.nextInt(numOfProducts);

                    // make sure the same random number isn't chosen more than once
                    while (alreadyReviewed.contains(randomProductI)) {
                        randomProductI = random.nextInt(numOfProducts);
                    }

                    product = products.get(randomProductI);

                    alreadyReviewed.add(randomProductI);

                    int rating = random.nextInt(5) + 1; // choose a random number between 1 and 5
                    String comment = faker.hitchhikersGuideToTheGalaxy().marvinQuote();
                    if (comment.length() > 255) comment = comment.substring(0, 250) + "...";
                    review = new Review(curr_user, product, rating, comment);
                    reviewRepository.save(review);
                    product.addReview(review);
                    user.addReview(review);
                    System.out.println("\tadding review: " + review);
                    reviews.add(review);
                }

                // Have users follow each other
                numOfFollowing = random.nextInt(numOfUsers - 1); // minus 1 because a person can't follow themselves
                System.out.println("\n\tgonna follow " + numOfFollowing + " people");
                for (int i = 0; i < numOfFollowing; i++) {
                    randomUserI = random.nextInt(numOfUsers);
                    while (alreadyFollowed.contains(randomUserI)) {
                        randomUserI = random.nextInt(numOfUsers);
                    }

                    alreadyFollowed.add(randomUserI);
                    users.get(randomUserI).addFollowing(curr_user);
                    System.out.println("\t\tfollowing user " + users.get(randomUserI).getUsername());
                }
            }

            // save all data in the repos
            reviewRepository.saveAll(reviews);
            userRepository.saveAll(users);
            productRepository.saveAll(products);

            System.out.println("==================== DONE GENERATING DATA ====================");
        };
    }
}

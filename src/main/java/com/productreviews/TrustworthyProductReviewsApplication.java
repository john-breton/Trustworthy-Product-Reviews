package com.productreviews;

import com.github.javafaker.Faker;
import com.github.javafaker.FunnyName;
import com.productreviews.models.Product;
import com.productreviews.models.Review;
import com.productreviews.models.User;
import com.productreviews.models.UserRegistrationDto;
import com.productreviews.repositories.ProductRepository;
import com.productreviews.repositories.ReviewRepository;
import com.productreviews.repositories.UserRepository;
import com.productreviews.services.UserService;
import com.productreviews.services.UserServiceImpl;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * Entry point for the application. For local development,
 * navigate to localhost:8080 upon launch.
 */
@SpringBootApplication
public class TrustworthyProductReviewsApplication {

    @Autowired
    UserRepository userRepository;

    public static void main(String[] args) {
        SpringApplication.run(TrustworthyProductReviewsApplication.class, args);
    }


    public static int num_of_users = 10;
    /**
     * CommandLineRunner a special bean that lets you execute some logic
     * after the application context is loaded and started.
     *
     * This one in particular is used to populate the database with dummy data
     * @param userRepository
     * @param productRepository
     * @return
     */
    @Bean
    public CommandLineRunner populate(UserRepository userRepository, ProductRepository productRepository,
                                      ReviewRepository reviewRepository, UserServiceImpl userService){
        return args -> {
            Faker faker = new Faker();

            // Create a user we can login with
            User user = null;
            ArrayList<User> users = new ArrayList<>();

            UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
            userRegistrationDto.setUsername("Team21");
            userRegistrationDto.setPassword("password");
            user = userService.save(userRegistrationDto);
            users.add(user);

            // Create the users
            for (int i = 0; i < num_of_users; i++){
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

            int num_of_products = products.size();
            int num_of_users = users.size();
            int num_of_following, num_reviews , random_product_i, random_user_i, index = 0;

            ArrayList<Integer> already_reviewed, already_followed;

            ArrayList<Review> reviews = new ArrayList<>();

            Review review;

            for (User curr_user: users) {

                System.out.println("****** curr user is " + curr_user);
                // empty it for next user
                already_reviewed = new ArrayList<>();
                already_followed = new ArrayList<>();
                already_followed.add(index); // add curr user to already followed so that they dont try following themselves
                index++;

                // generate a random number to determine how many products they're gonna review
                num_reviews = random.nextInt(num_of_products);
                System.out.println("Gonna review " + num_reviews + " products");
                // Create reviews for each user
                for (int i = 0; i < num_reviews; i++){
                    random_product_i = random.nextInt(num_of_products);

                    // make sure the same random number isn't chosen more than once
                    while (already_reviewed.contains(random_product_i)){
                        random_product_i = random.nextInt(num_of_products);
                    }

                    already_reviewed.add(random_product_i);

                    int rating = random.nextInt(5) + 1; // choose a random number between 1 and 5
                    String comment = faker.hitchhikersGuideToTheGalaxy().marvinQuote();
                    if (comment.length() > 255 ) comment = comment.substring(0, 250) + "...";
                    review = new Review(curr_user, products.get(random_product_i), rating, comment);
                    reviewRepository.save(review);
                    System.out.println("\tadding review: " + review);
                    reviews.add(review);

                }

                // Have users follow each other
                num_of_following = random.nextInt(num_of_users - 1); // minus 1 because a person can't follow themselves
                System.out.println("\n\tgonna follow " + num_of_following + " people");
                for (int i = 0; i < num_of_following; i++){
                    random_user_i = random.nextInt(num_of_users);
                    while (already_followed.contains(random_user_i)){
                        System.out.println("random user id = " +  random_user_i);

                        random_user_i = random.nextInt(num_of_following);
                    }

                    already_followed.add(random_user_i);
                    users.get(random_user_i).addFollowing(curr_user);
                    System.out.println("\t\tfollowing user " + users.get(random_user_i));
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

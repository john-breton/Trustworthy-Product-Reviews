package models;

import com.productreviews.models.Product;
import com.productreviews.models.Review;
import com.productreviews.models.common.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.parameters.P;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * This class implements basic functionality tests to confirm
 * the behaviour of the Product model used in the application.
 */
public class TestProductModel {

    private Product product;

    @BeforeEach
    public void setUp() {
        product = new Product();
    }

    /**
     * Generic test to ensure the implementation of the
     * auto-generated getters and setters of the product
     * class.
     */
    @Test
    public void testProductSettersAndGetters() {
        int id = 1;
        String name = "Test";
        String description = "This is a test";
        String image = "image.png";
        Category book = Category.BOOK;

        product.setId(id);
        product.setName(name);
        product.setDescription(description);
        product.setImage(image);
        product.setCategory(book);

        assertEquals(id, product.getId());
        assertEquals(name, product.getName());
        assertEquals(description, product.getDescription());
        assertEquals(image, product.getImage());
        assertEquals(book, product.getCategory());
    }

    /**
     * A test that covers any review related functionality
     * contained within the product class.
     */
    @Test
    public void testProductReviews() {
        Review review = new Review(null, null, 2, null);
        Review review2 = new Review(null, null, 4, null);
        ArrayList<Review> reviews = new ArrayList<>();
        reviews.add(review);

        assertEquals(0, product.getReviews().size());
        product.addReview(review);
        product.updateAverageRating();
        assertEquals(1, product.getReviews().size());
        product.addReview(review2);
        product.updateAverageRating();
        assertEquals(2, product.getReviews().size());
        assertEquals(3.0, product.getAverageRating());
        product.setReviews(reviews);
        product.updateAverageRating();
        assertEquals(1, product.getReviews().size());
        assertEquals(2.0, product.getAverageRating());
    }

    /**
     * A test that covers all potential constructor calls
     * to the product class.
     */
    @Test
    public void testProductConstructors() {
        String name = "test";
        String image = "test.jpg";
        String description = "this is a test";
        Category book = Category.BOOK;
        String url = "test";

        Product product1 = new Product(name, image);
        Product product2 = new Product(name, image, 2L);
        Product product3 = new Product(name, image, 2L, description, book);
        Product product4 = new Product(name, image, description, book, url);
        Product product5 = new Product(name, image, book, 2L);

        assertEquals(0, product.getId());
        assertNull(product.getImage());
        assertNull(product.getName());
        assertNull(product.getDescription());
        assertEquals(0, product.getReviews().size());
        assertNull(product.getCategory());
        assertNull(product.getUrl());

        assertEquals(0, product1.getId());
        assertEquals(image, product1.getImage());
        assertEquals(name, product1.getName());
        assertNull(product1.getDescription());
        assertEquals(0, product1.getReviews().size());
        assertNull(product.getCategory());
        assertNull(product2.getUrl());

        assertEquals(2, product2.getId());
        assertEquals(image, product2.getImage());
        assertEquals(name, product2.getName());
        assertNull(product2.getDescription());
        assertEquals(0, product2.getReviews().size());
        assertNull(product.getCategory());
        assertNull(product3.getUrl());

        assertEquals(2, product3.getId());
        assertEquals(image, product3.getImage());
        assertEquals(name, product3.getName());
        assertEquals(description, product3.getDescription());
        assertEquals(0, product3.getReviews().size());
        assertEquals(book, product3.getCategory());

        assertEquals(0, product4.getId());
        assertEquals(image, product4.getImage());
        assertEquals(name, product4.getName());
        assertEquals(description, product4.getDescription());
        assertEquals(0, product4.getReviews().size());
        assertEquals(book, product4.getCategory());
        assertEquals(url, product4.getUrl());

        assertEquals(2, product5.getId());
        assertEquals(image, product5.getImage());
        assertEquals(name, product5.getName());
        assertNull(product5.getDescription());
        assertEquals(0, product5.getReviews().size());
        assertEquals(book, product5.getCategory());
        assertNull(product5.getUrl());
    }

}

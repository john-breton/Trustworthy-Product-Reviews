package models;

import com.productreviews.models.Product;
import com.productreviews.models.Review;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

        product.setId(id);
        product.setName(name);
        product.setDescription(description);
        product.setImage(image);

        assertEquals(id, product.getId());
        assertEquals(name, product.getName());
        assertEquals(description, product.getDescription());
        assertEquals(image, product.getImage());
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
        assertEquals(1, product.getReviews().size());
        product.addReview(review2);
        assertEquals(2, product.getReviews().size());
        assertEquals("3.00", product.getAverageRating());
        product.setReviews(reviews);
        assertEquals(1, product.getReviews().size());
        assertEquals("2.00", product.getAverageRating());
    }

    /**
     * A test that covers all potential constructor calls
     * to the product class.
     */
    @Test
    public void testProductConstructors() {
        String name = "test";
        String image = "test.jpg";
        String description="test";
        String category = Product.CATEGORY1;

        Product product1 = new Product(name, image, description, category);
        Product product2 = new Product(name, image, category, 2L);

        assertEquals(0, product.getId());
        assertNull(product.getImage());
        assertNull(product.getName());
        assertNull(product.getDescription());
        assertEquals(0, product.getReviews().size());

        assertEquals(0, product1.getId());
        assertEquals(image, product1.getImage());
        assertEquals(name, product1.getName());
        assertNull(product1.getDescription());
        assertEquals(0, product1.getReviews().size());

        assertEquals(2, product2.getId());
        assertEquals(image, product2.getImage());
        assertEquals(name, product2.getName());
        assertNull(product2.getDescription());
        assertEquals(0, product2.getReviews().size());
    }

}

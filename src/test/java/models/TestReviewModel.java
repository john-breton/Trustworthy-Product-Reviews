package models;

import com.productreviews.models.Product;
import com.productreviews.models.Review;
import com.productreviews.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * This class implements basic functionality tests to confirm
 * the behaviour of the Review model used in the application.
 */
public class TestReviewModel {

    private Review review;

    @BeforeEach
    public void setUp() {
        review = new Review();
    }

    /**
     * Generic test to ensure the implementation of the
     * auto-generated getters and setters of the review
     * class.
     */
    @Test
    public void testReviewSettersAndGetters() {
        int id = 1;
        int score = 2;
        User user = new User();
        Product asscoiatedProduct = new Product();
        String content = "This is a test";

        review.setId(id);
        review.setScore(score);
        review.setUser(user);
        review.setAssociatedProduct(asscoiatedProduct);
        review.setContent(content);

        assertEquals(id, review.getId());
        assertEquals(score, review.getScore());
        assertEquals(user, review.getUser());
        assertEquals(asscoiatedProduct, review.getAssociatedProduct());
        assertEquals(content, review.getContent());
    }

    /**
     * A test that covers all potential constructor calls
     * to the review class.
     */
    @Test
    public void testReviewConstructors() {
        User user = new User();
        Product product = new Product();
        int rating = 2;
        String content = "This is a test";

        Review review1 = new Review(user, product, rating, content);

        assertEquals(0, review.getId());
        assertEquals(0, review.getScore());
        assertNull(review.getUser());
        assertNull(review.getAssociatedProduct());
        assertNull(review.getContent());

        assertEquals(0, review1.getId());
        assertEquals(2, review1.getScore());
        assertEquals(user, review1.getUser());
        assertEquals(product, review1.getAssociatedProduct());
        assertEquals(content, review1.getContent());
    }

}

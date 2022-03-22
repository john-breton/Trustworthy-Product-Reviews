package models;

import com.productreviews.models.Product;
import com.productreviews.models.Review;
import com.productreviews.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class implements basic functionality tests to confirm
 * the behaviour of the User model used in the application.
 */
public class TestUserModel {

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
    }

    /**
     * Generic test to ensure the implementation of the
     * auto-generated getters and setters of the user
     * class.
     */
    @Test
    public void testUserSettersAndGetters() {
        int id = 1;
        String username = "Test";
        String password = "test";

        user.setId(id);
        user.setUsername(username);
        user.setPassword(password);

        assertEquals(id, user.getId());
        assertEquals(username, user.getUsername());
        assertEquals(password, user.getPassword());
    }

    /**
     * A test that covers any review related functionality
     * contained within the user class.
     */
    @Test
    public void testUserReviews() {
        Review review1 = new Review(null, null, 2, null);
        review1.setId(1);
        Review review2 = new Review(null, null, 4, null);
        review2.setId(2);
        ArrayList<Review> reviews = new ArrayList<>();
        reviews.add(review1);

        assertEquals(0, user.getReviews().size());
        user.addReview(review1);
        assertEquals(1, user.getReviews().size());
        user.addReview(review2);
        assertEquals(2, user.getReviews().size());
        user.setReviews(reviews);
        assertEquals(1, user.getReviews().size());

        assertTrue(user.hasReview(1L));
        assertFalse(user.hasReview(2L));
    }

    /**
     * A test that covers any following list related
     * functionality contained within the user class.
     */
    @Test
    public void testUserFollowingList() {
        User user1 = new User("test", 2L);
        User user2 = new User("different user", 4L);

        assertEquals(0, user.getFollowingList().size());

        user.addFollowing(user1);
        assertEquals(1, user.getFollowingList().size());

        assertTrue(user.isFollowing(user1));
        assertFalse(user.isFollowing(user2));

        user.unFollow(user1);
        assertEquals(0, user.getFollowingList().size());
        assertFalse(user.isFollowing(user1));
    }

    /**
     * A test that covers all potential constructor calls
     * to the user class.
     */
    @Test
    public void testUserConstructors() {
        long id = 1L;
        String username = "Test";
        String password = "test";

        User user1 = new User(username);
        User user2 = new User(username, password);
        User user3 = new User(username, id);

        assertEquals(0, user.getId());
        assertNull(user.getUsername());
        assertNull(user.getPassword());

        assertEquals(0, user.getId());
        assertEquals(username, user1.getUsername());
        assertNull(user.getPassword());

        assertEquals(0, user2.getId());
        assertEquals(username, user2.getUsername());
        assertEquals(password, user2.getPassword());

        assertEquals(1, user3.getId());
        assertEquals(username, user3.getUsername());
        assertNull(user3.getPassword());
    }

    /**
     * A test to exercise the Jaccard Distance calculations that
     * can be performed between the review scores of one user
     * and the review scores of another user.
     */
    @Test
    public void testUserJaccardDistance() {
        User user1 = new User("john", "john");
        User user2 = new User("John2", "John2");

        // Base case, both review lists are empty, so distance should be 0 by definition.
        assertEquals(0, user1.getJaccardDistanceReviews(user2));
        // Shouldn't matter which user is first
        assertEquals(0, user2.getJaccardDistanceReviews(user1));

        Product product = new Product();
        Product product2 = new Product();
        Review rev1 = new Review(user1, product, 4, "Cool");
        Review rev2 = new Review(user2, product, 5, "Nice");
        Review rev3 = new Review(user1, product, 1, "not nice");
        Review rev4 = new Review(user1, product2, 1, "also not nice");
        Review rev5 = new Review(user2, product2, 4, "It's alright");
        Review rev6 = new Review(user2, product2, 1, "It's bad");
        Review rev7 = new Review(user2, product2, 2, "Not good");
        user1.addReview(rev1);
        user1.addReview(rev3);
        user1.addReview(rev4);
        user2.addReview(rev2);
        user2.addReview(rev5);
        user2.addReview(rev6);

        // Assert Jaccard distance is what is expected.
        assertEquals(0.5, user1.getJaccardDistanceReviews(user2));
        assertEquals(0.5, user2.getJaccardDistanceReviews(user1));

        // Modify the review lists and try again.
        user2.addReview(rev7);

        assertEquals(0.6, user1.getJaccardDistanceReviews(user2));
        assertEquals(0.6, user2.getJaccardDistanceReviews(user1));

        // Try for two identical review sets
        user2.addReview(rev1);
        user2.addReview(rev3);
        user2.addReview(rev4);
        user1.addReview(rev2);
        user1.addReview(rev5);
        user1.addReview(rev6);
        user1.addReview(rev7);

        assertEquals(0.0, user1.getJaccardDistanceReviews(user2));
        assertEquals(0.0, user2.getJaccardDistanceReviews(user1));
    }
}

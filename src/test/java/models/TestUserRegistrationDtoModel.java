package models;

import com.productreviews.models.UserRegistrationDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class implements basic functionality tests to confirm
 * the behaviour of the UserDtoModel model used in the application.
 */
public class TestUserRegistrationDtoModel {

    private UserRegistrationDto userDto;

    @BeforeEach
    public void setUp() {
        userDto = new UserRegistrationDto();
    }

    /**
     * Generic test to ensure the implementation of the
     * auto-generated getters and setters of the
     * UserRegistrationDto class.
     */
    @Test
    public void testUserDtoSettersAndGetters() {
        String username = "Test";
        String password = "test";

        userDto.setUsername(username);
        userDto.setPassword(password);

        assertEquals(username, userDto.getUsername());
        assertEquals(password, userDto.getPassword());
    }
}

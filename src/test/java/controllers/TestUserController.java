package controllers;

import com.productreviews.TrustworthyProductReviewsApplication;
import com.productreviews.models.User;
import com.productreviews.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * This class tests the UserController endpoints.
 * It is configured to use the spring security setup,
 * as the UserController is protected against unauthenticated users.
 */
@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest(classes = {TrustworthyProductReviewsApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestUserController {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UserRepository userRepository;

    private MockMvc mvc;

    private static final String USERNAME = "testuser";
    private static final String PASSWORD = "testpassword";

    /**
     * Explicitly configure the filter chain before every test to set up MockMvc.
     * Also, save the mock user to the repository
     */
    @BeforeEach
    public void setup() {
        User user = new User();
        user.setUsername(USERNAME);
        user.setPassword(PASSWORD);
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
        userRepository.save(user);
    }

    /**
     * Delete all records in the repository after each test so that the user objects don't interfere with eachother.
     */
    @AfterEach
    public void resetRepo() {
        userRepository.deleteAll();
    }

    /**
     * Test that the product page is rendered with the correct user
     *
     * @throws Exception
     */
    @Test
    @WithMockUser(username = USERNAME)
    public void testProductsPage() throws Exception {
        mvc
                .perform(get("/user/products")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(model().hasNoErrors())
                .andExpect(content().string(containsString(USERNAME)))
                .andExpect(status().isOk());
    }

    /**
     * Test that the people page is rendered with the correct user.
     *
     * @throws Exception
     */
    @Test
    @WithMockUser(username = USERNAME)
    public void testPeoplePage() throws Exception {
        mvc
                .perform(get("/user/people")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(model().hasNoErrors())
                .andExpect(content().string(containsString(USERNAME)))
                .andExpect(status().isOk());
    }

    /**
     * Test that the user page is rendered with the correct user.
     *
     * @throws Exception
     */
    @Test
    @WithMockUser(username = USERNAME)
    public void testPersonPage() throws Exception {
        mvc
                .perform(get("/user/"+USERNAME)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(model().hasNoErrors())
                .andExpect(content().string(containsString(USERNAME)))
                .andExpect(status().isOk());
    }
}

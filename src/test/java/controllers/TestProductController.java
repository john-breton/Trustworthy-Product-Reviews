package controllers;

import com.productreviews.TrustworthyProductReviewsApplication;
import com.productreviews.models.Product;
import com.productreviews.models.User;
import com.productreviews.models.common.Category;
import com.productreviews.repositories.ProductRepository;
import com.productreviews.repositories.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
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
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
@WithMockUser
@SpringBootTest(classes = {TrustworthyProductReviewsApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestProductController {

    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void setup() {
        userRepository.deleteAll();
        Product product = new Product();
        product.setName("name1");
        product.setId(1L);
        product.setCategory(Category.BOOK);
        productRepository.save(product);
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    /**
     * Delete all records in the repository after each test so that the user objects don't interfere with eachother.
     */
    @AfterEach
    public void cleanUp() {
        userRepository.deleteAll();
        productRepository.deleteAll();
    }


    /**
     * Test that a product page is rendered for a product and can use the product page
     * to add reviews to the product
     *
     * @throws Exception Thrown if a GET request is unsuccessful
     */
    @Test
    @WithMockUser(username = "testuser")
    public void ProductPageTest() throws Exception {

        //user authentication
        userRepository.save(new User("testuser", "testpassword"));
        System.out.println(formLogin());

        //access the product page and check that there initially no reviews
        mockMvc.perform(get("/product/1")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(model().hasNoErrors())
                .andExpect(content().string(containsString("No Reviews")));

        //go the home page
        mockMvc.perform(get("/home")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(model().hasNoErrors())
                .andExpect(status().is2xxSuccessful());

        //check that the product is on the home page
        mockMvc.perform(get("/home")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(model().hasNoErrors())
                .andExpect(content().string(containsString("name1")));
    }

}
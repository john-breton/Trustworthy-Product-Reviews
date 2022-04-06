package controllers;

import com.productreviews.TrustworthyProductReviewsApplication;
import com.productreviews.controllers.RegistrationController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = {TrustworthyProductReviewsApplication.class})
@AutoConfigureMockMvc(addFilters = false)
@WithMockUser
public class TestRegistrationController {

    @Autowired
    private MockMvc mockMvc;

    /**
     * Test that a new user can be added to the application
     * @throws Exception Thrown if a GET request is unsuccessful
     */
    @Test
    public void addValidNewUser() throws Exception {
        String username = "TestUsername";
        String password = "TestPassword";

        //ensure it loads
        mockMvc.perform(get("/registration"))
               .andExpect(model().attributeExists("user"))
               .andExpect(status().isOk());

        //try to add user with values and receive success
        mockMvc.perform(post("/registration")
                            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                            .param("username", username)
                            .param("password", password))
               .andExpect(model().hasNoErrors())
               .andExpect(status().is3xxRedirection());

        // check the database to make sure the user is present
        mockMvc.perform(get("/users"))
               .andExpect(content().string(containsString("\"username\" : \""+ username + "\"")))
               .andExpect(status().isOk());
    }

    /**
     * Tests that a duplicate user cannot be added to the application and ensures the error is displayed correctly
     * @throws Exception Thrown if a GET request is unsuccessful
     */
    @Test
    public void addDuplicateUser() throws Exception {
        String username = "SecondUsername";
        String password = "TestPassword";

        //ensure it loads
        mockMvc.perform(get("/registration"))
               .andExpect(model().attributeExists("user"))
               .andExpect(status().isOk());

        //try to add user with values and receive success
        mockMvc.perform(post("/registration")
                            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                            .param("username", username)
                            .param("password", password))
               .andExpect(model().hasNoErrors())
               .andExpect(status().is3xxRedirection());

        // check the database to make sure the user is present
        mockMvc.perform(get("/users"))
               .andExpect(content().string(containsString("\"username\" : \""+ username + "\"")))
               .andExpect(status().isOk());

        //try to add user again with values and receive 200 (meaning not successful and prompting user to try again)
        mockMvc.perform(post("/registration")
                            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                            .param("username", username)
                            .param("password", password))
               .andExpect(model().hasErrors())
               .andExpect(model().attributeHasErrors("user"))
               .andExpect(content().string(containsString("<span class=\"badge alert-warning\" >" +
                                                       "There is already an account registered with that username</span>")))
               .andExpect(status().is2xxSuccessful());
    }
}

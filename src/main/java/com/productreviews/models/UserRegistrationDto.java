package com.productreviews.models;

import javax.validation.constraints.NotEmpty;

/**
 * UserRegistrationDto is a minimal model class used strictly
 * for user authentication purposes and is required for Spring.
 */
public class UserRegistrationDto {

    /**
     * A required username to identify the user account.
     */
    @NotEmpty
    private String username;

    /**
     * A required password to authenticate the user account.
     */
    @NotEmpty
    private String password;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}

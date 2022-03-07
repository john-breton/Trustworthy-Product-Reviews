package com.productreviews.Services;

import com.productreviews.Models.User;
import com.productreviews.Models.UserRegistrationDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User findByUsername(String username);

    User save(UserRegistrationDto registration);
}

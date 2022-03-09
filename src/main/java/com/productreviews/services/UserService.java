package com.productreviews.services;

import com.productreviews.models.User;
import com.productreviews.models.UserRegistrationDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User findByUsername(String username);

    User save(UserRegistrationDto registration);
}

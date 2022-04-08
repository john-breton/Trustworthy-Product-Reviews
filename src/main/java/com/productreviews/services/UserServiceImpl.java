package com.productreviews.services;

import com.productreviews.models.User;
import com.productreviews.models.UserRegistrationDto;
import com.productreviews.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Save a User to the system.
     *
     * @param registration The UserRegistrationDto containing the username and password
     *                     of the User being added to the system.
     * @return The new User object being saved to the system.
     */
    public User save(UserRegistrationDto registration) {
        User user = new User();
        user.setUsername(registration.getUsername());
        user.setPassword(passwordEncoder.encode(registration.getPassword()));
        return userRepository.save(user);
    }

    /**
     * Get a User by their username.
     *
     * @param username The username for the user we which to load
     * @return The UserDeatils of a given User.
     * @throws UsernameNotFoundException Thrown if the username is not within the system.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(), getAuthorities("USER")
        );
    }

    /**
     * Get the available actions of a user based on their assigned role.
     *
     * @param role The role for the user, as a String. Currently, only the USER roles exist,
     *             but this would allow for additional roles to be added such as ADMIN or SUPERUSER
     * @return The actions of a user based on the provided role, as a Singleton Collection.
     */
    private Collection<? extends GrantedAuthority> getAuthorities(String role) {
        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }

}
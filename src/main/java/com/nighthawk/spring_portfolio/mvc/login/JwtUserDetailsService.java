package com.nighthawk.spring_portfolio.mvc.login;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.nighthawk.spring_portfolio.mvc.flashcards.UserJpaRepository;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private UserJpaRepository repository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // Since two different classes of the same name are used, I have to create a user using the entire import name
        Optional<com.nighthawk.spring_portfolio.mvc.flashcards.User> user = repository.findByEmail(email);
        if (user.isPresent()) {
            return new User(user.get().getEmail(), user.get().getPasswordHash(), new ArrayList<>());
        } else {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        // TODO Auto-generated method stub
    }
    // Basically this can't be implemented until an actual user class is created to store usernames and passwords
}

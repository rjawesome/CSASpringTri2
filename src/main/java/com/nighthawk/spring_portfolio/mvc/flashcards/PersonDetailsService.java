package com.nighthawk.spring_portfolio.mvc.flashcards;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class PersonDetailsService implements UserDetailsService {
    
    @Autowired
    private PersonJpaRepository personJpaRepository;

    @Autowired
    private PersonRoleJpaRepository personRoleJpaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // TODO Auto-generated method stub
        Optional<Person> person = personJpaRepository.findByEmail(email);
        if (!person.isPresent()) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        person.get().getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });
        return new User(person.get().getEmail(), person.get().getPasswordHash(), authorities);
    }

    // Person stuff

    public List<Person> listAll() {
        return personJpaRepository.findAllByOrderByNameAsc();
    }
}

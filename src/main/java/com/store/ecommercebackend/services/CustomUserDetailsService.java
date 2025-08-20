package com.store.ecommercebackend.services;

import com.store.ecommercebackend.repositories.UserRepository;
import com.store.ecommercebackend.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var user = userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("Invalid credentials!..")
        );
        return new UserPrincipal(user);
    }

    public UserDetails loadUserByUserId (Long id) {
        var user = userRepository.findById(id).orElseThrow();
        return new UserPrincipal(user);
    }
}

package com.checkpoint.sis.service.impl;

import com.checkpoint.sis.enums.Status;
import com.checkpoint.sis.model.User;
import com.checkpoint.sis.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));


        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name()));

        // --- THIS IS THE FINAL FIX ---
        // We explicitly tell Spring Security if the account is enabled based on our custom status.
        boolean isEnabled = user.getStatus() == Status.APPROVED;

        // We use the more detailed constructor to pass the account status.
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                isEnabled, // is enabled?
                true, // is account non-expired?
                true, // is credentials non-expired?
                true, // is account non-locked?
                authorities
        );
    }
}


package com.example.bookingsystem.serviceInt;

import com.example.bookingsystem.model.User;
import com.example.bookingsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author samwel.wafula
 * Created on 25/01/2024
 * Time 15:49
 * Project BookingSystem
 */
@RequiredArgsConstructor
@Service
public class UserAuthService implements  UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) {
        Optional<User> userDetails = userRepository.findUserByPhoneNumber(username);
        return userDetails.orElse(null);
    }
}

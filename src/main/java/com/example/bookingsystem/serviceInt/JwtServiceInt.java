package com.example.bookingsystem.serviceInt;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author samwel.wafula
 * Created on 25/01/2024
 * Time 16:51
 * Project BookingSystem
 */
public interface JwtServiceInt {

    String generateToken(UserDetails userDetails);
}

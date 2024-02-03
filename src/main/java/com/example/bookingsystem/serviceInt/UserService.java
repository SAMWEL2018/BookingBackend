package com.example.bookingsystem.serviceInt;

import com.example.bookingsystem.model.CustomResponse;
import com.example.bookingsystem.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * @author samwel.wafula
 * Created on 25/01/2024
 * Time 11:36
 * Project BookingSystem
 */
public interface UserService {

    CustomResponse registerUser(User user);
    Object getUser(String phoneNumber,String password) throws JsonProcessingException;
}

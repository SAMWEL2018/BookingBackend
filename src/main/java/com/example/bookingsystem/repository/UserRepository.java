package com.example.bookingsystem.repository;

import com.example.bookingsystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author samwel.wafula
 * Created on 25/01/2024
 * Time 11:34
 * Project BookingSystem
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findUserByPhoneNumber(String phoneNumber);
}

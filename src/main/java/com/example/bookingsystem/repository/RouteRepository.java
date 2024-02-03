package com.example.bookingsystem.repository;

import com.example.bookingsystem.model.Route;
import com.example.bookingsystem.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author samwel.wafula
 * Created on 27/01/2024
 * Time 10:55
 * Project BookingSystem
 */
@Repository
public interface RouteRepository extends JpaRepository<Route,Integer> {

}

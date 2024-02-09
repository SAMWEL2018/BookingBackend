package com.example.bookingsystem.serviceImpl;

import com.example.bookingsystem.model.CustomResponse;
import com.example.bookingsystem.model.Route;
import com.example.bookingsystem.repository.Datalayer;
import com.example.bookingsystem.serviceInt.RouteServiceInt;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author samwel.wafula
 * Created on 27/01/2024
 * Time 10:59
 * Project BookingSystem
 */
@Service
@RequiredArgsConstructor
public class RouteServiceImpl implements RouteServiceInt {
    private final Datalayer datalayer;

    @Override
    public CustomResponse createRoute(Route route) {
        return datalayer.createRoute(route);
    }

    public Optional<Route> findRoute(int id) {
        return datalayer.findRouteById(id);
    }
}

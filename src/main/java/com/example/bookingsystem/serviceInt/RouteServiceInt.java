package com.example.bookingsystem.serviceInt;

import com.example.bookingsystem.model.CustomResponse;
import com.example.bookingsystem.model.Route;

/**
 * @author samwel.wafula
 * Created on 27/01/2024
 * Time 10:58
 * Project BookingSystem
 */
public interface RouteServiceInt {

    CustomResponse createRoute(Route route);
}

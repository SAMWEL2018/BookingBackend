package com.example.bookingsystem.serviceInt;

import com.example.bookingsystem.model.CustomResponse;
import com.example.bookingsystem.model.Ticket;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * @author samwel.wafula
 * Created on 26/01/2024
 * Time 09:58
 * Project BookingSystem
 */
public interface TicketServiceInt {
    CustomResponse bookTicket(Ticket ticket);

    Object getBookedTickets(int routeId) throws JsonProcessingException;
}

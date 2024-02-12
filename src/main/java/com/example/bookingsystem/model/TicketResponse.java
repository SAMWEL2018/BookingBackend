package com.example.bookingsystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)

/**
 * @author samwel.wafula
 * Created on 27/01/2024
 * Time 12:47
 * Project BookingSystem
 */
@Builder
@Data
public class TicketResponse implements Serializable {
    private String seatNo;
}

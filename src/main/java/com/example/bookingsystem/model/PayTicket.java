package com.example.bookingsystem.model;

import lombok.Builder;
import lombok.Data;

/**
 * @author samwel.wafula
 * Created on 27/01/2024
 * Time 09:37
 * Project BookingSystem
 */

@Builder
@Data
public class PayTicket {

    private String amount;
    private String phoneNumber;
    private int ticketNo;
}

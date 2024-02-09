package com.example.bookingsystem.model;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PayTicket {

    private String amount;
    //@Required on Body
    private int routeId;
    //@Required on Body
    private String phoneNumber;
    //@Required on Body
    private int ticketNo;
}

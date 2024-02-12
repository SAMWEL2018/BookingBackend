package com.example.bookingsystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author samwel.wafula
 * Created on 25/01/2024
 * Time 11:11
 * Project BookingSystem
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomResponse implements Serializable {

    private String responseCode;
    private String responseDesc;
    private String token;
    private int ticketNo;
}

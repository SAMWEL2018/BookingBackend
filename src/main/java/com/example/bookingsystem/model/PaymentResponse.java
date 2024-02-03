package com.example.bookingsystem.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author samwel.wafula
 * Created on 01/02/2024
 * Time 12:36
 * Project BookingSystem
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponse {

    private String mpesaReceiptNumber;
    private boolean isPaid;
}
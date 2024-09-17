package com.example.bookingsystem.model;

import lombok.*;

/**
 * @author samwel.wafula
 * Created on 11/06/2024
 * Time 21:27
 * Project BookingSystem
 */
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountDetailObject {
    private String customerCode;
    private String productType;
    private String userId;
    private String customerAccount;
}

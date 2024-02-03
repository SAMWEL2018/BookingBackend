package com.example.bookingsystem.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author samwel.wafula
 * Created on 31/01/2024
 * Time 18:02
 * Project BookingSystem
 */
@Data
@Configuration
public class AppConfig {

    @Value("${app.MPESA_SERVICE_URL}")
    private String mpesaServiceUrl;
    @Value("${app.MPESA_SERVICE_URL_TRANSACTION}")
    private String mpesaServiceUrlTransaction;
}

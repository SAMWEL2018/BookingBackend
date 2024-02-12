package com.example.bookingsystem;

import com.example.bookingsystem.serviceImpl.TicketServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j

class BookingSystemApplicationTests {
    @Autowired
    private  TicketServiceImpl ticketService;

    @Test
    void contextLoads() {
    }

    @Test
    void initiateRefund() {
        log.info("res from refund {}", ticketService.initiateRefund(5));
    }

}

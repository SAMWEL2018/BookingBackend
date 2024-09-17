package com.example.bookingsystem;

import com.example.bookingsystem.httphandler.HttpService;
import com.example.bookingsystem.model.AccountDetailObject;
import com.example.bookingsystem.serviceImpl.TicketServiceImpl;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit4.SpringRunner;


@SpringBootTest
@Slf4j
class BookingSystemApplicationTests {
    //    private final TicketServiceImpl ticketService;
    @Autowired
    private HttpService httpService;

    @Test
    void contextLoads() {
    }

//    @Test
//    void initiateRefund() {
//        log.info("res from refund {}", ticketService.initiateRefund(5));
//    }

    @Test
    void getCustomerDetails() {
        String endpoint = "http://197.220.114.46:9005/adaptor/cbs/CRM/AccountDetails";
        JsonNode node = httpService.sendApiCallRequest(HttpMethod.POST, endpoint, AccountDetailObject.builder().customerAccount("string").customerCode("string").productType("string").userId("string").build());
        log.info("response {}", node);
    }
}

package com.example.bookingsystem.controller;

import com.example.bookingsystem.model.*;
import com.example.bookingsystem.repository.UserRepository;
import com.example.bookingsystem.serviceImpl.IdentifierImpl;
import com.example.bookingsystem.serviceImpl.RouteServiceImpl;
import com.example.bookingsystem.serviceImpl.TicketServiceImpl;
import com.example.bookingsystem.serviceInt.UserAuthService;
import com.example.bookingsystem.serviceInt.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author samwel.wafula
 * Created on 25/01/2024
 * Time 11:48
 * Project BookingSystem
 */
@RestController
@Component
@RequiredArgsConstructor
@Slf4j
@RequestMapping(value = "/api/")
public class ApiController {

    private final UserService userService;
    private final IdentifierImpl identifierService;
    private final UserAuthService userAuthService;
    private final UserRepository userRepository;
    private final TicketServiceImpl ticketService;
    private final RouteServiceImpl routeService;

    /**
     * ------------------------------------------------------------------------------------------
     * -------------------------------*** USER ***-------------------------------------------
     * ------------------------------------------------------------------------------------------
     */

    @RequestMapping(value = "/p1/register", method = RequestMethod.POST)
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        CustomResponse customResponse = userService.registerUser(user);
        return ResponseEntity.status(Integer.parseInt(customResponse.getResponseCode())).body(customResponse);
    }

    @RequestMapping(value = "p1/login", method = RequestMethod.POST)
    public ResponseEntity<?> loginUser(@RequestBody User user) throws JsonProcessingException {
        //System.out.println("======"+user.getPassword());

        if (user.getPassword() != null && user.getPhoneNumber() != null) {
            System.out.println("here");
            String phoneNumber = user.getPhoneNumber();
            String password = user.getPassword();
            Object response = userService.getUser(phoneNumber, password);
            JsonNode jsonNode = new ObjectMapper().readTree(new ObjectMapper().writeValueAsString(response));
            String code = jsonNode.get("responseCode").asText();

            return ResponseEntity.status(Integer.parseInt(code)).body(response);

        }
        return ResponseEntity.status(400).body(CustomResponse.builder().responseCode("400").responseDesc("BAD REQUEST, INPUT BOTH USERNAME AND PASSWORD"));
    }

    @RequestMapping(value = "s3/test", method = RequestMethod.GET)
    public ResponseEntity<?> getTest() {
        System.out.println("TEST");
        return ResponseEntity.ok("HEY");
    }

    /**
     * ------------------------------------------------------------------------------------------
     * -------------------------------*** TICKET ***-------------------------------------------
     * ------------------------------------------------------------------------------------------
     */
    @RequestMapping(value = "s3/bookTicket", method = RequestMethod.POST)
    public ResponseEntity<?> bookTicket(@RequestBody Ticket ticket, HttpServletRequest httpServletRequest) {
        //String subject = "0712321806";
        String phoneNo = identifierService.getSubject(httpServletRequest);
        log.info("phoneNo as Subject {}", phoneNo);
        Optional<User> user = userRepository.findUserByPhoneNumber(phoneNo);
        if (user.isPresent()) {
            String firstName = user.get().getFirstName();
            String lastName = user.get().getLastName();
            Ticket ticket1 = Ticket.builder()
                    .firstName(firstName)
                    .lastName(lastName)
                    .seatNo(ticket.getSeatNo())
                    .phoneNumber(user.get().getPhoneNumber())
                    .routeId(ticket.getRouteId())
                    .build();
            CustomResponse customResponse = ticketService.bookTicket(ticket1);
            return ResponseEntity.status(Integer.parseInt(customResponse.getResponseCode())).body(customResponse);
        }

        return ResponseEntity.status(401).body(CustomResponse.builder().responseCode("401").responseDesc("UNEXPECTED ERROR ON CONTROLLER"));
    }

    @RequestMapping(value = "s3/payTicket", method = RequestMethod.POST)
    public ResponseEntity<?> payTicket(@RequestBody PayTicket payTicket) {
        JsonNode jsonNode = ticketService.sendPayRequestToMpesaService(payTicket);
        if (jsonNode != null) {
            return ResponseEntity.status(Integer.parseInt(jsonNode.get("responseCode").asText())).body(jsonNode);
        }
        return ResponseEntity.status(400).body(CustomResponse.builder().responseCode("400").responseDesc("AMOUNT OR PHONE NO OR TICKET NO IS NULL"));
    }

    @RequestMapping(value = "p1/callTicketPayment/{ticketNo}", method = RequestMethod.POST)
    public ResponseEntity<?> callbackOnTicketPayment(@PathVariable("ticketNo") int ticketNo, @RequestBody PaymentResponse paymentResponse) {
        log.info("Hit Callback Controller");
        CustomResponse res = ticketService.processCallback(paymentResponse, ticketNo);
        return ResponseEntity.status(200).body(res);

    }

    @RequestMapping(value = "/s3/getPayBillReceiptPayment/{phoneNo}")
    public ResponseEntity<?> getPayBillReceipt(@PathVariable("phoneNo") String phoneNo, HttpServletRequest httpServletRequest) {
        String mobileNo = null;
        if (phoneNo != null) {
            mobileNo = phoneNo;
        } else {
            mobileNo = identifierService.getSubject(httpServletRequest);
        }
        try {
            CustomResponse response = ticketService.getTransactionPayment(mobileNo);
            log.info("PayBill payment request {}", new ObjectMapper().writeValueAsString(response));
            return ResponseEntity.status(Integer.parseInt(response.getResponseCode())).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(CustomResponse.builder().responseCode("400").responseDesc("Error OCCURRED"));
        }
    }


    @RequestMapping(value = "p1/getTicketsBooked/{routeId}", method = RequestMethod.GET)
    public ResponseEntity<?> getTicketsBooked(@PathVariable("routeId") int routeId) throws JsonProcessingException {
        List<TicketResponse> res = ticketService.getBookedTickets(routeId);
        if (!res.isEmpty()) {
            return ResponseEntity.status(200).body(res);

        } else {
            return ResponseEntity.status(200).body(CustomResponse.builder().responseCode("200").responseDesc("No Booked Ticket!").build());
        }


    }

    /**
     * ------------------------------------------------------------------------------------------
     * -------------------------------*** ROUTE ***-------------------------------------------
     * ------------------------------------------------------------------------------------------
     */

    @RequestMapping(value = "p1/createRoute", method = RequestMethod.POST)
    public ResponseEntity<?> createRoute(@RequestBody Route route) {
        if (route != null) {
            try {
                CustomResponse customResponse = routeService.createRoute(route);
                return ResponseEntity.status(Integer.parseInt(customResponse.getResponseCode())).body(customResponse);
            } catch (Exception e) {
                return ResponseEntity.status(401).body(CustomResponse.builder().responseCode("401").responseDesc("ERROR OCCURRED IN ROUTE CREATION: " + e));
            }
        }
        return ResponseEntity.status(402).body(CustomResponse.builder().responseDesc("400").responseDesc("EMPTY ROUTE OBJECT"));

    }


}

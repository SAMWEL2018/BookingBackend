package com.example.bookingsystem.repository;

import com.example.bookingsystem.model.*;
import com.example.bookingsystem.model.Package;
import com.example.bookingsystem.serviceInt.UserAuthService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author samwel.wafula
 * Created on 25/01/2024
 * Time 11:38
 * Project BookingSystem
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class Datalayer {

    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;
    private final UserAuthService userAuthService;
    private final RouteRepository routeRepository;
    private final PackageRepository packageRepository;


    public CustomResponse createUser(User user) {
        userRepository.save(user);
        return CustomResponse.builder()
                .responseCode("200")
                .responseDesc("User Created")
                .build();
    }

    public Object getUser(String phoneNumber) throws JsonProcessingException {
        //Optional<User> user = userRepository.findUserByPhoneNumber(phoneNumber);
        UserDetails userDetail = userAuthService.loadUserByUsername(phoneNumber);
        log.info("userDetail res {}", new ObjectMapper().writeValueAsString(userDetail));
        if (userDetail != null) {
            log.info("user {}", new ObjectMapper().writeValueAsString(userDetail));
            return userDetail;
        } else {
            System.out.println("null user");
            return CustomResponse.builder()
                    .responseCode("404")
                    .responseDesc("User Not Found")
                    .build();
        }
    }

    public CustomResponse sendPackage(Package pac) {
        Package pc = packageRepository.save(pac);
        return CustomResponse.builder()
                .responseCode("200")
                .responseDesc("Package Booking Created Successfully")
                .ticketNo(pc.getPackageId())
                .build();
    }

    public CustomResponse createTicket(Ticket ticket) {
        Ticket t1 = ticketRepository.save(ticket);
        return CustomResponse.builder()
                .responseCode("200")
                .responseDesc("Ticket Created Successfully")
                .ticketNo(t1.getTicketNo())
                .build();
    }

    public Optional<Ticket> getTicket(int ticketNo) {
        return ticketRepository.getTicketByTicketNo(ticketNo);
    }

    public List<Ticket> getTickets() {
        return ticketRepository.getAllTickets();
    }

    public Optional<Ticket> updateTicket(int ticketNo, int routeId) {
        ticketRepository.updateTicket(ticketNo, routeId);
        return ticketRepository.getTicketByTicketNo(ticketNo);

    }

    public void updateTransactionIfStkSent(boolean isSent, int ticketNo) {
        ticketRepository.updateTicketIfStkSend(true, ticketNo);
    }

    public void updateTransactionIfTicketIdPaid(boolean isPaid, String mpesaReceiptNo, int ticketNo) {
        ticketRepository.updateIfTicketIsPaid(isPaid, mpesaReceiptNo, ticketNo);
    }

    public Optional<Ticket> getCustomerTicketNotPaidFor(String phoneNo, boolean isPaid) {
        return ticketRepository.getTicketByPhoneNumberAndTicketPaid(phoneNo, false);
    }

    public List<TicketResponse> getListOfBookedTickets(int routeId) throws JsonProcessingException {
        List<Ticket> tickets = ticketRepository.getTicketByRouteId(routeId);
        List<TicketResponse> rp = new ArrayList<>();
        for (Ticket t : tickets) {
            TicketResponse res = TicketResponse.builder().seatNo(t.getSeatNo()).build();
            rp.add(res);
        }
        return rp;

    }

    public CustomResponse createRoute(Route route) {
        routeRepository.save(route);
        return CustomResponse.builder()
                .responseCode("200")
                .responseDesc("Route Created Successfully")
                .build();
    }

    public Optional<Route> findRouteById(int id) {
        return routeRepository.findByRouteId(id);
    }

}

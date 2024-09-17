package com.example.bookingsystem.serviceImpl;

import com.example.bookingsystem.config.AppConfig;
import com.example.bookingsystem.config.CachingConfiguration;
import com.example.bookingsystem.httphandler.HttpService;
import com.example.bookingsystem.model.*;
import com.example.bookingsystem.repository.Datalayer;
import com.example.bookingsystem.serviceInt.TicketServiceInt;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Optional;

/**
 * @author samwel.wafula
 * Created on 26/01/2024
 * Time 10:12
 * Project BookingSystem
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class TicketServiceImpl implements TicketServiceInt {
    private final Datalayer datalayer;
    private final HttpService httpService;
    private final AppConfig appConfig;
    private final CachingConfiguration cachingConfiguration;

    @Override
    public CustomResponse bookTicket(Ticket ticket) {
        return datalayer.createTicket(ticket);
    }

    @Override

    public List<TicketResponse> getBookedTickets(int routeId) throws JsonProcessingException {
        return datalayer.getListOfBookedTickets(routeId);
    }

    public CustomResponse processCallback(PaymentResponse callBackResponse, int ticketNo) {
        datalayer.updateTransactionIfTicketIdPaid(callBackResponse.isPaid(), callBackResponse.getMpesaReceiptNumber(), ticketNo);
        log.info("callback processed and updated");
        return CustomResponse.builder().responseCode("200").responseDesc("CALLBACK PROCESSED").build();
    }

    public JsonNode sendPayRequestToMpesaService(PayTicket payTicket) {
        log.info("inside 1");
        if (payTicket.getAmount() != null && payTicket.getPhoneNumber() != null) {
            log.info("inside 2");
            Optional<Ticket> ticket = datalayer.getTicket(payTicket.getTicketNo());
            log.info("payTicket {}", payTicket);
            if (ticket.isPresent()) {
                JsonNode node = httpService.sendApiCallRequest(HttpMethod.POST, appConfig.getMpesaServiceUrl(), payTicket);
                log.info("STK SERVICE RESPONSE ON REQUEST TO PAY {} FOR {} WITH RESPONSE {}", payTicket.getTicketNo(), payTicket.getPhoneNumber(), node);
                if (node.get("responseCode").asText().equals("200")) {
                    datalayer.updateTransactionIfStkSent(true, payTicket.getTicketNo());
                }
                return node;
            }

        }
        return null;
    }

    public CustomResponse getTransactionPayment(String mobileNo) throws JsonProcessingException {
        Object node = httpService.sendApiCallToGetTransaction(HttpMethod.GET, appConfig.getMpesaServiceUrlTransaction() + mobileNo);
        JsonNode jsonNode = new ObjectMapper().readTree(new ObjectMapper().writeValueAsString(node));
        if (node != null) {
            Optional<Ticket> ticket = datalayer.getCustomerTicketNotPaidFor(jsonNode.get("phoneNumber").asText(), false);
            if (ticket.isPresent()) {
                datalayer.updateTransactionIfTicketIdPaid(true, jsonNode.get("mpesaReceiptNumber").asText(), ticket.get().getTicketNo());
                return CustomResponse.builder().responseCode("200").responseDesc("TICKET PAID").build();
            } else {
                return CustomResponse.builder().responseCode("400").responseDesc("PAYMENT FOUND BUT TICKET NOT FOUND").build();
            }

        }
        return CustomResponse.builder().responseCode("401").responseDesc("PAYMENT NOT FOUND").build();
    }

    //    @Cacheable(value = "tk")
    public Flux<List<Ticket>> getTickets() {
        return datalayer.getTicketsFlux();

    }

    public Optional<Ticket> getTicket(int ticketNo) {
        return datalayer.getTicket(ticketNo);
    }

    public Optional<Ticket> updateTicket(int ticketNo, int routeId) {
        try {
            cachingConfiguration.getSpecificTicket.invalidate(ticketNo);
            cachingConfiguration.getSpecificTicket.cleanUp();
            log.info("ticket cache invalidated");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return datalayer.updateTicket(ticketNo, routeId);
    }

//    public Flux<List<Ticket>> getCachedTicketsFlux() {
////        List<Ticket> tks = cachingConfiguration.bookedTicketsCache.getIfPresent("0");
////
////        if (tks != null) {
////            return tks;
////        } else {
//            log.info("Cache miss! ");
//            Flux<List<Ticket>> tickets = getTickets();
//            //cachingConfiguration.bookedTicketsCache.put("0", tickets);
//
//            return tickets;
//       // }
//    }

    public List<Ticket> getCachedTickets() {
        List<Ticket> tks = cachingConfiguration.bookedTicketsCache.getIfPresent("0");

        if (tks != null) {
            return tks;
        } else {
            log.info("Cache miss! ");
            List<Ticket> tickets = datalayer.getTickets();
            cachingConfiguration.bookedTicketsCache.put("0", tickets);

            return tickets;
        }
    }

    public Optional<Ticket> getCachedTicket(int ticketNo) {
        Optional<Ticket> ticket = cachingConfiguration.getSpecificTicket.getIfPresent(ticketNo);
        if (ticket != null) {
            return ticket;
        } else {
            log.info("Cache miss for the ticket");
            Optional<Ticket> tk = getTicket(ticketNo);
            cachingConfiguration.getSpecificTicket.put(ticketNo, tk);
            return tk;
        }

    }

    public CustomResponse cancelTicket(int ticketNo, boolean cancel) {
        datalayer.cancelTicket(ticketNo, cancel);
        return CustomResponse.builder().responseCode("200").responseDesc("ticket cancelled").build();
    }

    public CustomResponse initiateRefund(int ticketNo) {
        CustomResponse customResponse = null;
        Optional<Ticket> ticket = getTicket(ticketNo);
        if (ticket.isPresent()) {
            int routeId = ticket.get().getRouteId();
            Optional<Route> route = datalayer.findRouteById(routeId);
            if (route.isPresent()) {
                String ticketPrice = route.get().getTicketPrice();
                double price = Double.parseDouble(ticketPrice);
                double finalPrice = 0.2 * price;
                log.info("Refund Price 20 % OFF {}", finalPrice);

                try {
                    JsonNode node = httpService.sendApiCallRequest(HttpMethod.POST, appConfig.getMpesaServiceB2CUrl(), PayTicket.builder().phoneNumber(ticket.get().getPhoneNumber()).amount(String.valueOf(finalPrice)).build());
                    customResponse = new ObjectMapper().readValue(new ObjectMapper().writeValueAsString(node), new TypeReference<CustomResponse>() {
                    });
                    log.info("response on B2C {}", node);
                    return customResponse;
                } catch (Exception e) {
                    log.error("Error on Sending B2C REQ {}", e.getMessage());
                    throw new RuntimeException(e);

                }


            }
        }
        return null;
    }
}

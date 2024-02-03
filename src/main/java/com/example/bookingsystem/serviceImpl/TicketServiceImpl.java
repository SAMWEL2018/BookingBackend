package com.example.bookingsystem.serviceImpl;

import com.example.bookingsystem.config.AppConfig;
import com.example.bookingsystem.httphandler.HttpService;
import com.example.bookingsystem.model.*;
import com.example.bookingsystem.repository.Datalayer;
import com.example.bookingsystem.serviceInt.TicketServiceInt;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

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
        if (payTicket.getAmount() != null && payTicket.getPhoneNumber() != null) {
            Optional<Ticket> ticket = datalayer.getTicket(payTicket.getTicketNo());
            if (ticket.isPresent()) {
                JsonNode node = httpService.sendApiCallRequest(HttpMethod.POST, appConfig.getMpesaServiceUrl(), payTicket);
                log.info("STK SERVICE RESPONSE ON TICKER {} FOR {} WITH RESPONSE {}", payTicket.getTicketNo(), payTicket.getPhoneNumber(), node);
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


}

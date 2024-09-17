package com.example.bookingsystem.httphandler;

import com.example.bookingsystem.model.AccountDetailObject;
import com.example.bookingsystem.model.PayTicket;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author samwel.wafula
 * Created on 31/01/2024
 * Time 17:48
 * Project BookingSystem
 */

@Service
@RequiredArgsConstructor
public class HttpService {
    private final WebClient webClient;

    public JsonNode sendApiCallRequest(HttpMethod httpMethod, String url, PayTicket payTicket) {
        return webClient.method(httpMethod)
                .uri(url)
                .bodyValue(payTicket)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();
    }
    public JsonNode sendApiCallRequest(HttpMethod httpMethod, String url, AccountDetailObject accountDetailObject) {
        return webClient.method(httpMethod)
                .uri(url)
                .bodyValue(accountDetailObject)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();
    }

    public Object sendApiCallToGetTransaction(HttpMethod httpMethod, String url) {
        return webClient.method(httpMethod)
                .uri(url)
                .retrieve()
                .bodyToMono(Object.class)
                .block();
    }
}

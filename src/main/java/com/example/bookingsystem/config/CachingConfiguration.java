package com.example.bookingsystem.config;

import com.example.bookingsystem.model.Ticket;
import com.example.bookingsystem.repository.Datalayer;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author samwel.wafula
 * Created on 10/02/2024
 * Time 08:51
 * Project BookingSystem
 */
//@Configuration
//@EnableCaching
@Component
@RequiredArgsConstructor
public class CachingConfiguration {

    private final Datalayer datalayer;


//    Caffeine<Object, Object> caffeine() {
//        return Caffeine.newBuilder()
//                .expireAfterAccess(Duration.ofMillis(60000));
//    }
//
//    @Bean
//    public CacheManager cacheManager() {
//        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
//        cacheManager.setCaffeine(caffeine());
//        return cacheManager;
//
//    }

//    public Cache<String,List<Ticket>> bookedTicketsCache(){
//        return Caffeine.newBuilder()
//                .expireAfterWrite(1, TimeUnit.MINUTES)
//                .maximumSize(100)
//                .build();
//    }

    public Cache<String, List<Ticket>> bookedTicketsCache = Caffeine.newBuilder()
            .expireAfterWrite(1, TimeUnit.MINUTES)
            .maximumSize(100)
            .build();

//    public Cache<String, List<Ticket>> refreshBookedTicketsCache = Caffeine.newBuilder()
//            .refreshAfterWrite(1, TimeUnit.MINUTES)
//            .maximumSize(100)
//            .build(k -> datalayer.getTickets());

    public Cache<Integer, Optional<Ticket>> getSpecificTicket = Caffeine.newBuilder()
            .expireAfterWrite(1, TimeUnit.MINUTES)
            .maximumSize(100)
            .build();
}

package com.example.bookingsystem.serviceImpl;

import com.example.bookingsystem.serviceInt.UserAuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author samwel.wafula
 * Created on 26/01/2024
 * Time 10:23
 * Project BookingSystem
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class IdentifierImpl {
    private final JwtServiceImpl jwtService;
    private final UserAuthService userAuthService;

    public String getSubject(HttpServletRequest request) {

        String headers = request.getHeader("Authorization");
        String subject = null;
        if (StringUtils.isNotEmpty(headers)) {
            String token = headers.substring(7);
            try {
                subject = jwtService.getUsernameFromToken(token);
                log.info("subject {}", subject);
            } catch (Exception e) {
                System.out.println("Error " + e);
                throw e;
            }
        }
        return subject;

    }
}

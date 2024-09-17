package com.example.bookingsystem.security;

import com.example.bookingsystem.serviceImpl.JwtServiceImpl;
import com.example.bookingsystem.serviceInt.UserAuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * @author samwel.wafula
 * Created on 25/01/2024
 * Time 15:47
 * Project BookingSystem
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {

    private final JwtServiceImpl jwtService;
    private final UserAuthService userAuthService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        CachedBodyHttpServletRequest cachedBodyHttpServletRequest = new CachedBodyHttpServletRequest(request);

        if (StringUtils.isEmpty(header) || !StringUtils.startsWith(header, "Bearer")) {
            filterChain.doFilter(cachedBodyHttpServletRequest, response);
            return;
        }
        String jwtToken = header.substring(7);
        String phoneNumber = null;
        phoneNumber = jwtService.getUserNameFromJwtToken(jwtToken);
        getUserAndCreateSecurityContext(cachedBodyHttpServletRequest, response, filterChain, phoneNumber, jwtToken, cachedBodyHttpServletRequest);

    }

    private void getUserAndCreateSecurityContext(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain, String phoneNumber, String jwtToken, CachedBodyHttpServletRequest cachedBodyHttpServletRequest) throws IOException, ServletException {
        if (StringUtils.isNotEmpty(phoneNumber) && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = userAuthService.loadUserByUsername(phoneNumber);
            Date date = jwtService.getExpirationDateOfToken(jwtToken);
            LocalDateTime localDateTime = LocalDateTime.now();
            Date CurrentDate = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
            log.info("current {}", CurrentDate);
            log.info("token Expiry Date {}", date);

            if (CurrentDate.after(date)) {
                log.info("token expired");
                System.out.println("expired");

            } else {
                System.out.println("valid Token");
                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                securityContext.setAuthentication(auth);
                SecurityContextHolder.setContext(securityContext);
                filterChain.doFilter(cachedBodyHttpServletRequest, response);
            }

        }
    }
}

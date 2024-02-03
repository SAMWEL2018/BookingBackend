package com.example.bookingsystem.serviceImpl;

import com.example.bookingsystem.model.CustomResponse;
import com.example.bookingsystem.model.Role;
import com.example.bookingsystem.model.User;
import com.example.bookingsystem.repository.Datalayer;
import com.example.bookingsystem.serviceInt.UserAuthService;
import com.example.bookingsystem.serviceInt.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author samwel.wafula
 * Created on 25/01/2024
 * Time 11:37
 * Project BookingSystem
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final Datalayer datalayer;
    private final UserAuthService userAuthService;
    private final JwtServiceImpl jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Override
    public CustomResponse registerUser(User user) {
        User candid = User.builder()
                .email(user.getEmail())
                .gender(user.getGender())
                .lastName(user.getLastName())
                .firstName(user.getFirstName())
                .idNumber(user.getIdNumber())
                .phoneNumber(user.getPhoneNumber())
                .password(passwordEncoder.encode(user.getPassword()))
                .role(Role.USER)
                .build();
        return datalayer.createUser(candid);
    }

    @Override
    public Object getUser(String phoneNumber, String password) throws JsonProcessingException {
        Object response = datalayer.getUser(phoneNumber);
        //User userD= new ObjectMapper().readValue(new ObjectMapper().writeValueAsString(response), User.class);
        JsonNode node = new ObjectMapper().readTree(new ObjectMapper().writeValueAsString(response));
        log.info("node from res {}", node);
        if (node.has("responseCode")) {
            return CustomResponse.builder()
                    .responseCode(node.get("responseCode").asText())
                    .responseDesc(node.get("responseDesc").asText())
                    .build();
        }
        try {
            System.out.println("Proceeding to Auth MG");
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(phoneNumber, password));
            UserDetails user = userAuthService.loadUserByUsername(phoneNumber);
            String token = jwtService.generateToken(user);
            log.info("token {}", token);
            return CustomResponse.builder().responseCode("200").responseDesc("VALID CREDENTIAL").token(token).build();
        } catch (BadCredentialsException badCredentialsException) {
            System.out.println("Exception " + badCredentialsException);
            return CustomResponse.builder().responseCode("401").responseDesc("Incorrect Password").build();
        } catch (Exception e) {
            return CustomResponse.builder().responseCode("400").responseDesc("UNEXPECTED ERROR").build();
        }

    }
}

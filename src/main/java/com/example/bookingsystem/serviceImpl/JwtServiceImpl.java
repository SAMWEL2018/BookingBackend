package com.example.bookingsystem.serviceImpl;

import com.example.bookingsystem.serviceInt.JwtServiceInt;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;

/**
 * @author samwel.wafula
 * Created on 25/01/2024
 * Time 16:52
 * Project BookingSystem
 */
@Service
public class JwtServiceImpl implements JwtServiceInt {
    String SecretKey = "413F4428472B4B6250655368566D5970337336763979244226452948404D6351";
    @Override
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000*60*30))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    public Key getSigningKey(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(SecretKey));
    }

    public Date getExpirationDateOfToken(String token){
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build()
                .parseClaimsJws(token).getBody().getExpiration();
    }

    public String getUsernameFromToken(String token){
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }


}

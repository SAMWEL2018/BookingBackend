package com.example.bookingsystem.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Date;

/**
 * @author samwel.wafula
 * Created on 27/01/2024
 * Time 10:22
 * Project BookingSystem
 */
@Data
@Entity
@Table(name = "tbl_routes")
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int routeId;
    @Basic
    private Date travelDate;
    @Basic
    private String timeTravelDate;
    @Basic
    private String origin;
    @Basic
    private String destination;
    //@@
    @Basic
    private String ticketPrice;


}

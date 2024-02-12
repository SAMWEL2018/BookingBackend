package com.example.bookingsystem.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author samwel.wafula
 * Created on 25/01/2024
 * Time 15:07
 * Project BookingSystem
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "tbl_tickets")
public class Ticket implements Serializable {

    //* From fronted marked with @@

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_no")
    private int ticketNo;

    @Basic
    //@@
    private int routeId;
    @Basic
    //@@
    private String seatNo;
    @Basic
    @CreationTimestamp
    private Timestamp dateCreated;
    @Basic
    private String firstName;
    @Basic
    private String lastName;
    @Basic
    private String phoneNumber;
    @Basic
    @Column(name = "is_ticket_paid")
    private boolean ticketPaid=false;
    @Basic
    @Column(name = "is_push_sent")
    private boolean isStkPushSent=false;
    @Basic
    @Column(name = "mpesa_receipt_number")
    private String mpesaReceiptNumber;
}

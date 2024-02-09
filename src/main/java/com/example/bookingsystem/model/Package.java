package com.example.bookingsystem.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * @author samwel.wafula
 * Created on 05/02/2024
 * Time 15:18
 * Project BookingSystem
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "tbl_packages")
public class Package {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int packageId;
    //@Required on Body
    @Enumerated(EnumType.STRING)
    private PackageType packageDelicacy;
    //@Required on Body
    private String packageName;
    //@Required on Body
    private String packageDescription;
    //@Required on Body
    private int packageRoute;
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

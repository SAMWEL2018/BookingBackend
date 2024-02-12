package com.example.bookingsystem.repository;

import com.example.bookingsystem.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author samwel.wafula
 * Created on 26/01/2024
 * Time 10:15
 * Project BookingSystem
 */
public interface TicketRepository extends JpaRepository<Ticket, Integer> {

    List<Ticket> getTicketByRouteId(int routeId);

    @Query(value = "update tbl_tickets set route_id=:routeId where ticket_no=:ticketNo", nativeQuery = true)
    @Modifying
    @Transactional
    void updateTicket(@Param("ticketNo") int ticketNo, @Param("routeId") int routeId);

    @Query(value = "select * from tbl_tickets", nativeQuery = true)
    List<Ticket> getAllTickets();

    Optional<Ticket> getTicketByTicketNo(int ticketNo);

    @Query(value = "update tbl_tickets set is_push_sent=:isPushSent where ticket_no=:ticketNo", nativeQuery = true)
    @Modifying
    @Transactional
    void updateTicketIfStkSend(@Param("isPushSent") boolean isPushSent, @Param("ticketNo") int ticketNo);

    @Query(value = "update tbl_tickets set is_ticket_paid=:isTicketPaid, mpesa_receipt_number=:mpesaReceiptNo where ticket_no=:ticketNo", nativeQuery = true)
    @Modifying
    @Transactional
    void updateIfTicketIsPaid(@Param("isTicketPaid") boolean is_ticket_paid, @Param("mpesaReceiptNo") String mpesaReceiptNo, @Param("ticketNo") int ticketNo);

    Optional<Ticket> getTicketByPhoneNumberAndTicketPaid(String phoneNo, boolean isTicketPaid);


}

package com.example.bookingsystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

/**
 * @author samwel.wafula
 * Created on 25/01/2024
 * Time 11:01
 * Project BookingSystem
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@Entity
@Table(name = "tbl_users")
public class User implements UserDetails {

    @Id
    private String phoneNumber;

    @Basic
    private String idNumber;

    @Basic
    @Column(name = "first_name")
    private String firstName;

    @Basic
    @Column(name = "email")
    private String email;
    @Basic
    @Column(name = "last_name")
    private String lastName;
    @Basic
    @Column(name = "gender")
    private String gender;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Basic
    @Column(name = "password")
    private String password;

    @Basic
    @Column(name = "date_created")
    @CreationTimestamp
    private Timestamp dateCreated;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(
                "USER"
        ));
    }

    @Override
    public String getUsername() {
        return phoneNumber;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

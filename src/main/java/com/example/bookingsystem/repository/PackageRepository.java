package com.example.bookingsystem.repository;

import com.example.bookingsystem.model.Package;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author samwel.wafula
 * Created on 05/02/2024
 * Time 17:06
 * Project BookingSystem
 */
@Repository
public interface PackageRepository extends JpaRepository<Package,Integer> {
}

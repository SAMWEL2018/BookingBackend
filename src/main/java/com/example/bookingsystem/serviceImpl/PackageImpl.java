package com.example.bookingsystem.serviceImpl;

import com.example.bookingsystem.model.CustomResponse;
import com.example.bookingsystem.model.Package;
import com.example.bookingsystem.repository.Datalayer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author samwel.wafula
 * Created on 08/02/2024
 * Time 16:12
 * Project BookingSystem
 */

@Service
@RequiredArgsConstructor
public class PackageImpl {
    private final Datalayer datalayer;

    public CustomResponse sendPackage(Package pac) {
        return datalayer.sendPackage(pac);
    }
}

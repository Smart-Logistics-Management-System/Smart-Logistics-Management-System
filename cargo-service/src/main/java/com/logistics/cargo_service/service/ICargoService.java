package com.logistics.cargo_service.service;

import com.logistics.cargo_service.model.Cargo;

import java.util.Optional;

public interface ICargoService {
    Cargo createCargo(Cargo cargo);
    Optional<Cargo> getCargoByTrackingNumber(String trackingNumber);
    java.util.List<Cargo> getAllCargos();
    void updateCargoStatus(String trackingNumber, String status);
}

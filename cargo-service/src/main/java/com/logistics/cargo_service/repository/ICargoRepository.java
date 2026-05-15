package com.logistics.cargo_service.repository;

import com.logistics.cargo_service.model.Cargo;

import java.util.Optional;

public interface ICargoRepository {
    void save(Cargo cargo);
    Optional<Cargo> findByTrackingNumber(String trackingNumber);
    java.util.List<Cargo> findAll();
}

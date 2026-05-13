package com.logistics.cargo_service.service;

import com.logistics.cargo_service.model.Cargo;

import java.util.Optional;

public interface ICargoService {
    Cargo createCargo(Cargo cargo);
}

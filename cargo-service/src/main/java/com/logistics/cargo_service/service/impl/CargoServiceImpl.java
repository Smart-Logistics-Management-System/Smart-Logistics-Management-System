package com.logistics.cargo_service.service.impl;

import com.logistics.cargo_service.model.Cargo;
import com.logistics.cargo_service.repository.ICargoRepository;
import com.logistics.cargo_service.service.ICargoService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class CargoServiceImpl implements ICargoService {
    private  final ICargoRepository iCargoRepository;

    public CargoServiceImpl(ICargoRepository iCargoRepository){
        this.iCargoRepository = iCargoRepository;
    }

    @Override
    public Cargo createCargo(Cargo cargo){
        cargo.setTrackingNumber(UUID.randomUUID().toString());
        cargo.setStatus("PENDING");
        if (cargo.getEstimatedDeliveryDate() == null) {
            cargo.setEstimatedDeliveryDate(LocalDateTime.now().plusDays(3));
        }
        iCargoRepository.save(cargo);
        return cargo;
    }
}

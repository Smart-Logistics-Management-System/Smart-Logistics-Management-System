package com.logistics.cargo_service.service.impl;

import com.logistics.cargo_service.model.Cargo;
import com.logistics.cargo_service.model.CargoStatus;
import com.logistics.cargo_service.repository.ICargoRepository;
import com.logistics.cargo_service.service.ICargoService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class CargoServiceImpl implements ICargoService {
    private  final ICargoRepository iCargoRepository;

    public CargoServiceImpl(ICargoRepository iCargoRepository){
        this.iCargoRepository = iCargoRepository;
    }
    @Override
    public Optional<Cargo> getCargoByTrackingNumber(String trackingNumber) {
        return iCargoRepository.findByTrackingNumber(trackingNumber);
    }

    @Override
    public Cargo createCargo(Cargo cargo){
        cargo.setTrackingNumber(UUID.randomUUID().toString());
        cargo.setStatus(CargoStatus.PENDING);
        if (cargo.getEstimatedDeliveryDate() == null) {
            cargo.setEstimatedDeliveryDate(LocalDateTime.now().plusDays(3));
        }
        iCargoRepository.save(cargo);
        return cargo;
    }
}

package com.logistics.cargo_service;

import org.junit.jupiter.api.Test;

public class CargoRepositoryTest {
    @Test
    public void testSaveCargo(){
        Cargo newCargo = new Cargo();
        newCargo.setTrackingNumber("TRK-123456");
        newCargo.setSenderId(1L);
        newCargo.setReceiverId(2L);
        newCargo.setWeight(2.5);
        newCargo.setStatus("PREPARING");
        ICargoRepository cargoRepository = new CargoRepositoryImpl(null);
        cargoRepository.save(newCargo);
        Cargo savedCargo = cargoRepository.findByTrackingNumber("TRK-14532026").orElse(null);
        assertNotNull(savedCargo, "Kargo veritabanından çekilemedi, null geldi!");
        assertEquals("TRK-14532026", savedCargo.getTrackingNumber());
        assertEquals("PREPARING", savedCargo.getStatus());
    }
}

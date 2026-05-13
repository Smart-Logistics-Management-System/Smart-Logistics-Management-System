package com.logistics.cargo_service;

import com.logistics.cargo_service.model.Cargo;
import org.junit.jupiter.api.Test;

public class CargoServiceTest {
    @Mock
    private ICargoRepository cargoRepository;

    @InjectMocks
    private CargoServiceImpl cargoService;
    @Test
    public void shouldCreateCargoWithTrackingNumber() {
        Cargo newCargo = new Cargo();
        newCargo.setSenderId(1L);
        newCargo.setReceiverId(2L);
        newCargo.setWeight(5.0);

        Cargo createdCargo = cargoService.createCargo(newCargo);
        assertNotNull(createdCargo.getTrackingNumber(), "Takip numarası boş olmamalı!");
        assertEquals("PENDING", createdCargo.getStatus(), "Yeni kargonun durumu PENDING olmalı!");
        verify(cargoRepository, times(1)).save(any(Cargo.class));
    }
}

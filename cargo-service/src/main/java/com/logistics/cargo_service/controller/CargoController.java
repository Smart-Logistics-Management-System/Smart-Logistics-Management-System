package com.logistics.cargo_service.controller;

import com.logistics.cargo_service.model.Cargo;
import com.logistics.cargo_service.service.ICargoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cargo")
public class CargoController {
    private final ICargoService cargoService;

    public CargoController(ICargoService cargoService){
        this.cargoService = cargoService;
    }
    @PostMapping
    public ResponseEntity<Cargo> createCargo(@RequestBody Cargo cargo){
        Cargo createdCargo = cargoService.createCargo(cargo);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCargo);
    }
}

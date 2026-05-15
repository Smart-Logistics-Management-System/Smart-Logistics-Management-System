package com.logistics.cargo_service.controller;

import com.logistics.cargo_service.dto.CreateCargoRequest;
import com.logistics.cargo_service.model.Cargo;
import com.logistics.cargo_service.service.ICargoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cargo")
public class CargoController {
    private final ICargoService cargoService;

    public CargoController(ICargoService cargoService){
        this.cargoService = cargoService;
    }
    @PostMapping
    public ResponseEntity<Cargo> createCargo(@RequestBody CreateCargoRequest request){
        Cargo newCargo = new Cargo();
        newCargo.setSenderId(request.getSenderId());
        newCargo.setReceiverId(request.getReceiverId());
        newCargo.setWeight(request.getWeight());

        Cargo createdCargo = cargoService.createCargo(newCargo);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCargo);
    }
    @GetMapping("/{trackingNumber}")
    public ResponseEntity<Cargo> trackCargo(@PathVariable String trackingNumber) {
        return cargoService.getCargoByTrackingNumber(trackingNumber)
                .map(cargo -> ResponseEntity.ok(cargo))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<java.util.List<Cargo>> getAllCargos() {
        return ResponseEntity.ok(cargoService.getAllCargos());
    }

    @PostMapping("/status-update")
    public ResponseEntity<Void> updateStatus(@RequestBody com.logistics.cargo_service.dto.UpdateStatusRequest request) {
        try {
            cargoService.updateCargoStatus(request.getTrackingNumber(), request.getStatus());
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}

package com.logistics.cargo_service.service;

import org.springframework.stereotype.Component;

import java.util.UUID;
@Component
public class UuidTrackingNumberGenerator implements ITrackingNumberGenerator {
    @Override
    public String generate() {
        return UUID.randomUUID().toString();
    }
}

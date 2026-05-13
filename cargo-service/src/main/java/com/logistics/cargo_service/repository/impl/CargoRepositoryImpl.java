package com.logistics.cargo_service.repository.impl;

import com.logistics.cargo_service.model.Cargo;
import com.logistics.cargo_service.repository.ICargoRepository;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Optional;

public class CargoRepositoryImpl implements ICargoRepository {
    private final JdbcTemplate jdbcTemplate;

    public CargoRepositoryImpl(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public void save(Cargo cargo){
        String sql = "INSERT INTO cargo (tracking_number, sender_id, receiver_id, weight, status, current_location, estimated_delivery_date) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                cargo.getTrackingNumber(),
                cargo.getSenderId(),
                cargo.getReceiverId(),
                cargo.getWeight(),
                cargo.getStatus(),
                cargo.getCurrentLocation(),
                cargo.getEstimatedDeliveryDate()
        );
    }
    @Override
    public Optional<Cargo> findByTrackingNumber(String trackingNumber) {
        String sql = "SELECT * FROM cargo WHERE tracking_number =?";
        return jdbcTemplate.query(sql,cargoRowMapper,trackingNumber).stream().findFirst();
    }
    private final org.springframework.jdbc.core.RowMapper<Cargo> cargoRowMapper = (rs, rowNum) -> {
        Cargo cargo = new Cargo();
        cargo.setId(rs.getLong("id"));
        cargo.setTrackingNumber(rs.getString("tracking_number"));
        cargo.setSenderId(rs.getLong("sender_id"));
        cargo.setReceiverId(rs.getLong("receiver_id"));
        cargo.setWeight(rs.getDouble("weight"));
        cargo.setStatus(rs.getString("status"));
        cargo.setCurrentLocation(rs.getString("current_location"));

        java.sql.Timestamp deliveryDate = rs.getTimestamp("estimated_delivery_date");
        if (deliveryDate != null) {
            cargo.setEstimatedDeliveryDate(deliveryDate.toLocalDateTime());
        }

        return cargo;
    };

}

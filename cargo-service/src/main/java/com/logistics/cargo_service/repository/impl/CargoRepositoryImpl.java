package com.logistics.cargo_service.repository.impl;

import com.logistics.cargo_service.model.Cargo;
import com.logistics.cargo_service.model.CargoStatus;
import com.logistics.cargo_service.repository.ICargoRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public class CargoRepositoryImpl implements ICargoRepository {
    private final SimpleJdbcInsert simpleJdbcInsert;
    private static final String FIND_BY_TRACKING =
            "SELECT * FROM cargo WHERE tracking_number = ?";
    private final JdbcTemplate jdbcTemplate;

    public CargoRepositoryImpl(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("cargo")
                .usingGeneratedKeyColumns("id");
    }
    @Override
    public void save(Cargo cargo) {
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("tracking_number", cargo.getTrackingNumber())
                .addValue("sender_id", cargo.getSenderId())
                .addValue("receiver_id", cargo.getReceiverId())
                .addValue("weight", cargo.getWeight())
                .addValue("status", cargo.getStatus().name())
                .addValue("current_location", cargo.getCurrentLocation())
                .addValue("estimated_delivery_date", cargo.getEstimatedDeliveryDate());

        Number newId = simpleJdbcInsert.executeAndReturnKey(parameters);

        cargo.setId(newId.longValue());
    }
    @Override
    public Optional<Cargo> findByTrackingNumber(String trackingNumber) {
        return jdbcTemplate.query(FIND_BY_TRACKING,cargoRowMapper,trackingNumber).stream().findFirst();
    }
    private final RowMapper<Cargo> cargoRowMapper = (rs, rowNum) -> {
        Cargo cargo = new Cargo();
        cargo.setId(rs.getLong("id"));
        cargo.setTrackingNumber(rs.getString("tracking_number"));
        cargo.setSenderId(rs.getLong("sender_id"));
        cargo.setReceiverId(rs.getLong("receiver_id"));
        cargo.setWeight(rs.getDouble("weight"));
        cargo.setStatus(CargoStatus.valueOf(rs.getString("status")));
        cargo.setCurrentLocation(rs.getString("current_location"));

        java.sql.Timestamp deliveryDate = rs.getTimestamp("estimated_delivery_date");
        if (deliveryDate != null) {
            cargo.setEstimatedDeliveryDate(deliveryDate.toLocalDateTime());
        }

        return cargo;
    };

}

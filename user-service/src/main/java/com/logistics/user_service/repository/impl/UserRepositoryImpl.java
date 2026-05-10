package com.logistics.user_service.repository.impl;

import com.logistics.user_service.model.User;
import com.logistics.user_service.repository.IUserRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public class UserRepositoryImpl implements IUserRepository {
    private final JdbcTemplate jdbcTemplate;

    public UserRepositoryImpl(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(User user) {
        String sql = "INSERT INTO users(first_name ,last_name,email,role) VALUES(?,?,?,?) ";
        jdbcTemplate.update(sql,user.getFirstName(),user.getLastName(),user.getEmail(),user.getRole());
    }

    @Override
    public Optional<User> findById(long id) {
        return Optional.empty();
    }

    @Override
    public List<User> findAll() {
        return List.of();
    }

    @Override
    public void update(User user) {
        jdbcTemplate.update(sqlUpdate,user.getFirstName(),user.getLastName(),user.getEmail(),user.getRole(),user.getId());
    }

    @Override
    public void delete(Long id) {
        String sqlDelete = "DELETE FROM users WHERE id = ?";
        jdbcTemplate.update(sqlDelete ,id);
    }  String sqlUpdate = "UPDATE users SET first_name=?,last_name=?,email=?,role=? WHERE id =?";
      
}

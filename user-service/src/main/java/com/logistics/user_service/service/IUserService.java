package com.logistics.user_service.service;

import com.logistics.user_service.model.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    void save(User user);
    User findById(long id);
    List<User> findAll();
    Optional<User> findByEmail(String email);
    void update (User user);
    void delete(Long id);

}

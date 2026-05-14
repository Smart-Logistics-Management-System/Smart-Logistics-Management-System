package com.logistics.user_service.repository;
import com.logistics.user_service.model.User;
import java.util.List;
import java.util.Optional;

public interface IUserRepository {
    void save(User user);
    Optional<User> findById(long id);
    List<User> findAll();
    void update (User user);
    void delete(Long id);
}

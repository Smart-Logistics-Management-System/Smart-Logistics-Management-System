package com.logistics.user_service.service.impl;

import com.logistics.user_service.exception.UserNotFoundException;
import com.logistics.user_service.model.User;
import com.logistics.user_service.repository.IUserRepository;
import com.logistics.user_service.service.IUserService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements IUserService {
    private final IUserRepository userRepository;

    public UserServiceImpl(IUserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    @CacheEvict(value = "users", allEntries = true)
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    @Cacheable(value = "user", key = "#id")
    public User findById(long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Kullanıcı bulunamadı! ID: " + id));
    }

    @Override
    @Cacheable(value = "users")
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    @CacheEvict(value = "users", allEntries = true)
    public void update(User user) {
        userRepository.update(user);
    }

    @Override
    @CacheEvict(value = "users", allEntries = true)
    public void delete(Long id) {
        userRepository.delete(id);
    }
}

package com.logistics.user_service.controller;

import com.logistics.user_service.model.User;
import com.logistics.user_service.service.IUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController  {
    private final IUserService userService;

    public UserController(IUserService userService){
        this.userService = userService;
    }
    @PostMapping
    public ResponseEntity<Void> save(@RequestBody User user){
        userService.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).build();  // 201 durum kodu
    }
    @GetMapping
    public ResponseEntity<List<User>> findAll(){
        return ResponseEntity.ok(userService.findAll());
    }
    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id){
        Optional<User> optionalUser = userService.findById(id);

        if(optionalUser.isPresent()){
            return ResponseEntity.ok(optionalUser.get());
        }
        else{
            return  ResponseEntity.notFound().build();
        }
    }
}

package com.example.GameStore.controller;

import com.example.GameStore.entity.ChangePasswordRequest;
import com.example.GameStore.entity.ResetPasswordRequest;
import com.example.GameStore.entity.User;
import com.example.GameStore.service.user.UserServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserServiceImpl service;

    public UserController(UserServiceImpl service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User newUser = service.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Long userId) {
        User user = service.getUserById(userId);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUser() {
        List<User> userList = service.getAllUser();
        return ResponseEntity.ok(userList);
    }

    @GetMapping("/login")
    public ResponseEntity<User> login(@RequestParam String username, @RequestParam String password) {
        User user = service.login(username, password);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        service.deleteUser(userId);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tài khoản đã bị xóa !");
    }

    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable Long userId, @RequestBody User updateUser) {
        User user = service.updateUser(userId, updateUser);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/{userId}/changePassword")
    public ResponseEntity<String> changePassword(@PathVariable Long userId, @RequestBody ChangePasswordRequest request) {
        try {
            service.changePassword(userId, request);
            return ResponseEntity.ok("Password update successfully!");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


        @PutMapping("/forgot-password")
    public ResponseEntity<String> resetPassword(@RequestParam(name = "email") String email) {
        service.forgotPassword(email);
        return ResponseEntity.ok(" New password đã được gửi về email của bạn !");
    }


}

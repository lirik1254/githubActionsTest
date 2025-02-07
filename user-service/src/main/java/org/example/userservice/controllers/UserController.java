package org.example.userservice.controllers;


import org.example.userservice.models.Users;
import org.example.userservice.services.UserService;
import org.example.userservice.utils.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<Users> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("users/{id}")
    public Users getUserById(@PathVariable Integer id) {
        return userService.getUserById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    @PostMapping("users")
    public ResponseEntity<Users> createUser(@RequestBody Users user) {
        Users createdUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    // PUT: Обновить существующего пользователя
    @PutMapping("users/{id}")
    public ResponseEntity<Users> updateUser(@PathVariable Integer id, @RequestBody Users user) {
        Users updatedUser = userService.updateUser(id, user);
        return ResponseEntity.ok(updatedUser);
    }

    // DELETE: Удалить пользователя
    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Integer id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.status(HttpStatus.OK).body("User deleted successfully.");
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with id: " + id);
        }
    }
}

package com.example.controller;

import com.example.model.TaskDTO;
import com.example.model.UserDTO;
import com.example.model.Users;
import com.example.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:8080")
public class UserController {
    private final UserService userService;

    @GetMapping("/")
    public ResponseEntity<List<Users>> getAllUsers() {
        List<Users> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Users> getUserById(@PathVariable Long id) {
        Optional<Users> optionalUser = userService.getUserById(id);
        if (optionalUser.isPresent()) {
            Users users = optionalUser.get();
            return ResponseEntity.ok(users);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<Users> getUserByUsername(@PathVariable String username) {
        Optional<Users> optionalUser = userService.getUserByUsername(username);
        if (optionalUser.isPresent()) {
            Users users = optionalUser.get();
            return ResponseEntity.ok(users);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Users> saveNewUser(@RequestBody UserDTO userDTO) {
        Users newUsers = userService.saveNewUser(userDTO);
        return ResponseEntity.ok(newUsers);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Users> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        Users updatedUsers = userService.updateUser(id, userDTO);
        return ResponseEntity.ok(updatedUsers);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{userId}/tasks")
    public ResponseEntity<List<TaskDTO>> getUserTasks(@PathVariable Long userId) {
        List<TaskDTO> userTasks = userService.getUserTasks(userId);
        return ResponseEntity.ok(userTasks);
    }

    @PostMapping("/{userId}/tasks")
    public ResponseEntity<TaskDTO> addUserTask(@PathVariable Long userId, @RequestBody TaskDTO taskDTO) {
        TaskDTO addedTask = userService.addUserTask(userId, taskDTO);
        return ResponseEntity.ok(addedTask);
    }

    @PutMapping("/{userId}/tasks/{taskId}")
    public ResponseEntity<TaskDTO> updateUserTask(@PathVariable Long userId, @PathVariable Long taskId, @RequestBody TaskDTO taskDTO) {
        TaskDTO updatedTask = userService.updateUserTask(userId, taskId, taskDTO);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{userId}/tasks/{taskId}")
    public ResponseEntity<Void> deleteUserTask(@PathVariable Long userId, @PathVariable Long taskId) {
        userService.deleteUserTask(userId, taskId);
        return ResponseEntity.noContent().build();
    }

//    @PutMapping("/{userId}/taskLimit")
//    public ResponseEntity<?> setTaskLimit(@PathVariable Long userId, @RequestParam int limit) {
//        try {
//            userService.setTaskLimit(userId, limit);
//            return ResponseEntity.ok("Task limit set successfully.");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to set task limit.");
//        }
//    }





}

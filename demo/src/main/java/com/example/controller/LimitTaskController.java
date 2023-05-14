package com.example.controller;

import com.example.model.LimitTask;
import com.example.service.LimitTaskService;
import com.example.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/limit")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:8080")
public class LimitTaskController {
    private final LimitTaskService limitTaskService;

    private final UserService userService;

    @GetMapping("/")
    public ResponseEntity<?> getAllLimitTasks() {
        try {
            List<LimitTask> limitTasks = limitTaskService.getAllLimitTask();
            return new ResponseEntity<>(limitTasks, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{userId}/setTaskLimit")
    public ResponseEntity<String> setTaskLimit(@PathVariable Long userId, @RequestParam int limit) {
        try {
            limitTaskService.setTaskLimit(userId, limit);
            return new ResponseEntity<>("Task limit has been set for user with ID: " + userId, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to set task limit for user with ID: " + userId, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
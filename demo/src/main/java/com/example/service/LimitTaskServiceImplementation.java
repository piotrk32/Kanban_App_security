package com.example.service;

import com.example.model.LimitTask;
import com.example.model.Users;
import com.example.repository.LimitTaskRepository;
import com.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.spi.Limit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@RequiredArgsConstructor
public class LimitTaskServiceImplementation implements LimitTaskService {
    private final LimitTaskRepository limitTaskRepository;
    private final UserRepository userRepository;



    @Override
    @Transactional
    public List<LimitTask> getAllLimitTask() {
        return limitTaskRepository.findAll();

    }

    @Override
    @Transactional
    public void setTaskLimit(Long userId, int limit) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        LimitTask userLimit = user.getLimit();
        if (userLimit == null) {
            userLimit = new LimitTask();
            userLimit.setUser(user); // Ustawienie powiązanego użytkownika
        }
        userLimit.setTaskLimit(limit);
        user.setLimit(userLimit);

        userRepository.save(user);
    }
}
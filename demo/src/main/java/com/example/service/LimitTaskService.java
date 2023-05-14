package com.example.service;

import com.example.model.LimitTask;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface LimitTaskService {

    List<LimitTask> getAllLimitTask();

    void setTaskLimit(Long userId, int limit);
}

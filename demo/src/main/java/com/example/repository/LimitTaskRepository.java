package com.example.repository;

import com.example.model.LimitTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LimitTaskRepository extends JpaRepository<LimitTask, Long> {

    Optional<LimitTask> findById(Long id);


}

package com.example.service;

import com.example.model.TaskDTO;
import com.example.model.UserDTO;
import com.example.model.Users;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<Users> getAllUsers();

    Optional<Users> getUserById(Long id);

    Optional<Users> getUserByUsername(String username);

    Users saveNewUser(UserDTO userDTO);

    Users updateUser(Long id, UserDTO userDTO);

    void deleteUser(Long id);
    List<TaskDTO> getUserTasks(Long userId);

    TaskDTO addUserTask(Long userId, TaskDTO taskDTO);

    TaskDTO updateUserTask(Long userId, Long taskId, TaskDTO taskDTO);

    void deleteUserTask(Long userId, Long taskId);

    Users saveUser(Users user);
}

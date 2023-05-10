package com.example.service;

import com.example.model.*;
import com.example.model.Users;
import com.example.repository.ColumnKanbanRepository;
import com.example.repository.TaskRepository;
import com.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImplementation implements UserService {

    private final UserRepository userRepository;
    private final ColumnKanbanRepository columnKanbanRepository;

    private final TaskRepository taskRepository;

    @Override
    @Transactional
    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public Optional<Users> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    @Transactional
    public Optional<Users> getUserByUsername(String username) {
        return userRepository.findByEmail(username);
    }

    @Override
    @Transactional
    public Users saveNewUser(UserDTO userDTO) {
        Users users = convertUserDTOtoUser(userDTO);
        return userRepository.save(users);
    }
    public Users saveUser(Users user) {
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public Users updateUser(Long id, UserDTO userDTO) {
        Optional<Users> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            Users users = optionalUser.get();
            users.setFirstName(userDTO.getFirstName());
            users.setLastName(userDTO.getLastName());
            users.setPassword(userDTO.getPassword());
            // Set other properties from UserDTO to User
            return userRepository.save(users);
        } else {
            throw new RuntimeException("User not found with id: " + id);
        }
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public List<TaskDTO> getUserTasks(Long userId) {
        Users users = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        return convertTasksToTaskDTOs(users.getTasks());
    }

    @Override
    @Transactional
    public TaskDTO addUserTask(Long userId, TaskDTO taskDTO) {
        Users users = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        Task task = convertDTOToTask(taskDTO);
        task.setUsers(users);
        taskRepository.save(task);
        return taskDTO;
    }

    @Override
    @Transactional
    public TaskDTO updateUserTask(Long userId, Long taskId, TaskDTO taskDTO) {
        Users users = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + taskId));
        if (task.getUsers().getId().equals(userId)) {
            task.setTaskTitle(taskDTO.getTitle());
            task.setDescription(taskDTO.getDescription());
            task.setColor(taskDTO.getColor());
            task.setStatus(taskDTO.getStatus());
            taskRepository.save(task);
        } else {
            throw new RuntimeException("Task not assigned to user with id: " + userId);
        }
        return taskDTO;
    }

    @Override
    @Transactional
    public void deleteUserTask(Long userId, Long taskId) {
        Users users = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + taskId));
        if (task.getUsers().getId().equals(userId)) {
            taskRepository.delete(task);
        } else {
            throw new RuntimeException("Task not assigned to user with id: " + userId);
        }
    }






    //CONVERTING

    private Users convertUserDTOtoUser(UserDTO userDTO) {
        Users users = new Users();
        users.setFirstName(userDTO.getFirstName());
        users.setLastName(userDTO.getLastName());
        users.setPassword(userDTO.getPassword());
        users.setId(userDTO.getId());
        List<Task> l = new ArrayList<>();
        users.setTasks(l);
        return users;
    }

    private Task convertDTOToTask(TaskDTO taskDTO) {
        Task task = new Task();
        task.setTaskTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setColor(taskDTO.getColor());
        task.setStatus(taskDTO.getStatus());

        Optional<Users> optUser = userRepository.findById(taskDTO.getUser_id());
        if (!optUser.isPresent()) {
            throw new IllegalArgumentException("User with given id not exists.");
        }
        task.setUsers(optUser.get());
        Optional<ColumnKanban> optKanbanColumn = columnKanbanRepository.findById(taskDTO.getKanban_column_id());
        if (!optKanbanColumn.isPresent())
            throw new IllegalArgumentException("Column with given id not exists.");
        task.setColumnKanban(optKanbanColumn.get());
        return task;
    }

    private List<TaskDTO> convertTasksToTaskDTOs(List<Task> tasks) {
        List<TaskDTO> taskDTOs = new ArrayList<>();
        for (Task task : tasks) {
            TaskDTO taskDTO = new TaskDTO();
            taskDTO.setTitle(task.getTaskTitle());
            taskDTO.setDescription(task.getDescription());
            taskDTO.setColor(task.getColor());
            taskDTO.setStatus(task.getStatus());
            taskDTO.setUser_id(task.getUsers().getId());
            taskDTO.setKanban_column_id(task.getColumnKanban().getId());
            taskDTOs.add(taskDTO);
        }
        return taskDTOs;
    }




}

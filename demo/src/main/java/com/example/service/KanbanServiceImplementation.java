package com.example.service;


import com.example.model.*;
import com.example.model.Users;
import com.example.repository.KanbanRepository;
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
public class KanbanServiceImplementation implements KanbanService {

    private final TaskRepository taskRepository;
    private final KanbanRepository kanbanRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public List<Kanban> getAllKanbanBoards() {
        return kanbanRepository.findAll();

    }

    @Override
    @Transactional
    public Optional<Kanban> getKanbanById(Long id) {
        return kanbanRepository.findById(id);
    }

    @Override
    @Transactional
    public Optional<Kanban> getKanbanByTitle(String title) {
        return kanbanRepository.findByKanbanTitle(title);
    }

    @Override
    @Transactional
    public Kanban saveNewKanban(KanbanDTO kanbanDTO) {
        Kanban kanban = convertDTOToKanban(kanbanDTO);
        List<Users> users = convertDTOToUser(kanbanDTO.getUsers()); // Konwertuj UserDTO na User
        kanban.setUsers(userRepository.saveAll(users)); // Zapisz listę użytkowników do bazy danych
        return kanbanRepository.save(kanban);
    }

    @Override
    @Transactional
    public Kanban updateKanban(Long id, KanbanDTO kanbanDTO) {
        Optional<Kanban> optionalKanban = kanbanRepository.findById(id);
        if (optionalKanban.isPresent()) {
            Kanban kanban = optionalKanban.get();
            kanban.setKanbanTitle(kanbanDTO.getKanbanTitle());

            // Update columns
            List<ColumnKanban> columns = new ArrayList<>();
            for (ColumnKanbanDTO columnDTO : kanbanDTO.getColumnsKanban()) {
                Optional<ColumnKanban> optionalColumn = kanban.getColumnsKanban()
                        .stream()
                        .filter(c -> c.getId().equals(columnDTO.getId()))
                        .findFirst();

                ColumnKanban column;
                if (optionalColumn.isPresent()) {
                    column = optionalColumn.get();
                } else {
                    column = new ColumnKanban();
                    column.setKanban(kanban);
                }

                column.setColumnTitle(columnDTO.getColumnTitle());
                columns.add(column);
            }
            kanban.setColumnsKanban(columns);

            return kanbanRepository.save(kanban);
        } else {
            throw new RuntimeException("Kanban not found with id: " + id);
        }
    }

    @Override
    @Transactional
    public void deleteKanban(Kanban kanban) {
        kanbanRepository.delete(kanban);
    }

    //TOCHECK
    @Override
    @Transactional
    public Kanban addNewColumnToKanban(Long kanbanId, ColumnKanbanDTO columnKanbanDTO) {
        Optional<Kanban> kanban = kanbanRepository.findById(kanbanId);
        if (kanban.isPresent()){
            kanban.get().addColumn(convertDTOToColumn(columnKanbanDTO));
            return kanbanRepository.save(kanban.get());
        }
        else{
            //TODO ERROR
            System.out.println("ZLE");
            return new Kanban();
        }
    }

    private Kanban convertDTOToKanban(KanbanDTO kanbanDTO){
        Kanban kanban = new Kanban();
        kanban.setKanbanTitle(kanbanDTO.getKanbanTitle());
        kanban.setUsers(convertDTOToUser(kanbanDTO.getUsers()));
        return kanban;
    }

    @Override
    @Transactional
    public Kanban assignUserToKanban(Long kanbanId, Long userId) {
        Kanban kanban = kanbanRepository.findById(kanbanId)
                .orElseThrow(() -> new RuntimeException("Kanban not found with id: " + kanbanId));
        Users users = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        if (kanban.getUsers() == null) {
            kanban.setUsers(new ArrayList<>());
        }
        kanban.getUsers().add(users);
        return kanbanRepository.save(kanban);
    }

    @Override
    @Transactional
    public Kanban unassignUserFromKanban(Long kanbanId, Long userId) {
        Kanban kanban = kanbanRepository.findById(kanbanId)
                .orElseThrow(() -> new RuntimeException("Kanban not found with id: " + kanbanId));
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        kanban.getUsers().remove(user);
        return kanbanRepository.save(kanban);
    }
//    @Override
//    @Transactional
//    public User updateUser(User user) {
//        return userRepository.save(user);
//    }

    //CONVERTING
    private ColumnKanban convertDTOToColumn(ColumnKanbanDTO columnKanbanDTO) {
        ColumnKanban columnKanban = new ColumnKanban();
        columnKanban.setColumnTitle(columnKanbanDTO.getColumnTitle());
        columnKanban.setId(columnKanban.getId());
        return columnKanban;
    }
    private List<Users> convertDTOToUser(List<UserDTO> userDTOs) {
        List<Users> users = new ArrayList<>();
        for (UserDTO userDTO : userDTOs) {
            Users user = new Users();
            user.setId(userDTO.getId());
            user.setFirstName(userDTO.getFirstName());
            user.setLastName(userDTO.getLastName());
            user.setPassword(userDTO.getPassword());
            // Set tasks for the user
            if (userDTO.getTaskIds() != null) {
                List<Task> tasks = (List<Task>) taskRepository.findAllById(userDTO.getTaskIds());
                user.setTasks(tasks);
            }
             //Set kanban for the user
            if (userDTO.getKanbanId() != null) {
                Optional<Kanban> kanbanOptional = kanbanRepository.findById(userDTO.getKanbanId());
                kanbanOptional.ifPresent(user::setKanban);
            }
            users.add(user);
        }
        return users;
    }



}
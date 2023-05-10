package com.example.service;
import com.example.model.*;
import com.example.repository.ColumnKanbanRepository;
import com.example.repository.KanbanRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ColumnKanbanServiceImplementation implements ColumnKanbanService {

    private final ColumnKanbanRepository columnKanbanRepository;
    private final KanbanRepository kanbanRepository;
    @Override
    @Transactional
    public List<ColumnKanban> getAllColumns() {
        List<ColumnKanban> columnsList = new ArrayList<>();
        columnsList.addAll(columnKanbanRepository.findAll());
        return columnsList;
    }

    @Override
    @Transactional
    public Optional<ColumnKanban> getColumnById(Long id) {
        return columnKanbanRepository.findById(id);
    }

    @Override
    @Transactional
    public Optional<ColumnKanban> getColumnByTitle(String columnTitle) {
        return columnKanbanRepository.findByColumnTitle(columnTitle);
    }

    @Override
    @Transactional
    public ColumnKanban saveNewColumn(ColumnKanbanDTO columnKanbanDTO) {
        return columnKanbanRepository.save(convertDTOToColumn(columnKanbanDTO));
    }

    @Override
    @Transactional
    public ColumnKanban updateColumn(ColumnKanban oldColumn, ColumnKanbanDTO newTColumnDTO) {
        return columnKanbanRepository.save(updateColumnFromDTO(oldColumn, newTColumnDTO));
    }

    @Override
    @Transactional
    public void deleteColumn(ColumnKanban columnKanban) {
        columnKanbanRepository.delete(columnKanban);

    }
    //CHeCK
    @Override
    @Transactional
    public ColumnKanban addNewTaskToColumn(Long kanban_column_id, TaskDTO taskDTO) {
        Optional<ColumnKanban> optionalColumnKanban = columnKanbanRepository.findById(kanban_column_id);
        if (optionalColumnKanban.isPresent()) {
            ColumnKanban columnKanban = optionalColumnKanban.get();
            columnKanban.addTaskToColumn(convertDTOToTask(taskDTO));
            return columnKanbanRepository.save(columnKanban);
        } else {
            // Obsłuż brak kolumny Kanban z podanym ID, np. zgłoś wyjątek
            throw new NoSuchElementException("ColumnKanban with ID: " + kanban_column_id + " not found.");
        }
    }
    private Task convertDTOToTask(TaskDTO taskDTO) {
        Task task = new Task();
        task.setTaskTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setColor(taskDTO.getColor());
        task.setStatus(taskDTO.getStatus());
        return task;
    }
// do rozwazenia update taskow w kolumnie
    private ColumnKanban updateColumnFromDTO(ColumnKanban columnKanban, ColumnKanbanDTO columnKanbanDTO) {
        // Update column title
        if (Optional.ofNullable((columnKanbanDTO.getColumnTitle())).isPresent()) {
            columnKanban.setColumnTitle(columnKanbanDTO.getColumnTitle());
        }

        // Update kanban_id
        if (Optional.ofNullable((columnKanbanDTO.getKanban_id())).isPresent()) {
            Optional<Kanban> optionalKanban = kanbanRepository.findById(columnKanbanDTO.getKanban_id());
            if (optionalKanban.isPresent()) {
                columnKanban.setKanban(optionalKanban.get());
            } else {
                throw new EntityNotFoundException("Kanban not found with id: " + columnKanbanDTO.getKanban_id());
            }
    }



    // Update tasks list
    if (columnKanbanDTO.getTaskDTOList() != null) {
        List<Task> tasks = columnKanbanDTO.getTaskDTOList().stream()
                .map(taskDTO -> convertDTOToTask(taskDTO))
                .collect(Collectors.toList());
        columnKanban.setTasks(tasks);
    }

    return columnKanban;
}
    private ColumnKanban convertDTOToColumn(ColumnKanbanDTO columnKanbanDTO) {
        ColumnKanban columnKanban = new ColumnKanban();
        columnKanban.setColumnTitle(columnKanbanDTO.getColumnTitle());
        Optional<Kanban> optionalKanban = kanbanRepository.findById(columnKanbanDTO.getKanban_id());
        if (!optionalKanban.isPresent()) {
            throw new RuntimeException("Kanban not found with id: " + columnKanbanDTO.getKanban_id());
        }
        columnKanban.setKanban(optionalKanban.get());
        //CHECK
        List<Task> tasks = columnKanbanDTO.getTaskList().stream()
                .map(this::convertDTOToTask) // Assuming you have a method to convert TaskDTO to Task
                .collect(Collectors.toList());
        columnKanban.setTasks(tasks);
        return columnKanban;
    }

}

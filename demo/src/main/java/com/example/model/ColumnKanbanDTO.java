package com.example.model;

import lombok.*;

import java.util.List;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ColumnKanbanDTO {
    private Long id;
    private Long kanban_id;
    private String columnTitle;
    private List<TaskDTO> taskDTOList;

    private String columnColor;

    public List<TaskDTO> getTaskList() {
        return taskDTOList;
    }

}


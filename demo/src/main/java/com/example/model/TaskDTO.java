package com.example.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTO {

    private Long kanban_column_id;
    private String title;
    private String description;
    private String color;
    private TaskStatus status;
    private Long user_id;

}

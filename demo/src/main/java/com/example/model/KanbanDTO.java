package com.example.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KanbanDTO {
    private Long id;
    private String kanbanTitle;
    private List<ColumnKanbanDTO> columnsKanban;
    private List<UserDTO> users;
}

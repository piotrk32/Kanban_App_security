package com.example.model;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LimitTaskDTO {
    private Long id;
    private Users users;
    private LimitTask limit;

}

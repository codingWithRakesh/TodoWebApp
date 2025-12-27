package com.todoList.TodoListWebApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateResponseDto {
    private Long id;
    private String name;
    private String email;
}

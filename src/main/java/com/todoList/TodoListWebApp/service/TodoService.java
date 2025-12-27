package com.todoList.TodoListWebApp.service;

import com.todoList.TodoListWebApp.dto.TodoCreateDto;
import com.todoList.TodoListWebApp.dto.TodoResponseDto;
import org.jspecify.annotations.Nullable;

import java.util.List;

public interface TodoService {

    TodoResponseDto createNewTodo(Long userId,TodoCreateDto todoCreateDto);

    TodoResponseDto findTodoById(Long todoId);

    List<TodoResponseDto> findAllTodos();

    TodoResponseDto updateTodo(Long todoId, TodoCreateDto todoCreateDto);

    TodoResponseDto updatePartialTodo(Long todoId, TodoCreateDto todoCreateDto);

    void deleteTodoById(Long todoId);
}

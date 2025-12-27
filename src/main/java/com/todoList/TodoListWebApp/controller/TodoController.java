package com.todoList.TodoListWebApp.controller;

import com.todoList.TodoListWebApp.dto.TodoCreateDto;
import com.todoList.TodoListWebApp.dto.TodoResponseDto;
import com.todoList.TodoListWebApp.service.TodoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/todo")
public class TodoController {
    private final TodoService todoService;

    TodoController(TodoService todoService){
        this.todoService = todoService;
    }

    @PostMapping("/create/{userId}")
    public ResponseEntity<TodoResponseDto> createTodo(@PathVariable Long userId, @RequestBody TodoCreateDto todoCreateDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(todoService.createNewTodo(userId, todoCreateDto));
    }

    @GetMapping("/find/{todoId}")
    public ResponseEntity<TodoResponseDto> findById(@PathVariable Long todoId){
        return ResponseEntity.status(HttpStatus.OK).body(todoService.findTodoById(todoId));
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<TodoResponseDto>> findAll(){
        return ResponseEntity.status(HttpStatus.OK).body(todoService.findAllTodos());
    }

    @PutMapping("/update/{todoId}")
    public ResponseEntity<TodoResponseDto> updateTodo(@PathVariable Long todoId, @RequestBody TodoCreateDto todoCreateDto){
        return ResponseEntity.status(HttpStatus.OK).body(todoService.updateTodo(todoId,todoCreateDto));
    }

    @PatchMapping("/update/{todoId}")
    public ResponseEntity<TodoResponseDto> updatePartialTodo(@PathVariable Long todoId, @RequestBody TodoCreateDto todoCreateDto){
        return ResponseEntity.status(HttpStatus.OK).body(todoService.updatePartialTodo(todoId, todoCreateDto));
    }

    @DeleteMapping("/delete/{todoId}")
    public ResponseEntity<Void> deleteTodoById(@PathVariable Long todoId){
        todoService.deleteTodoById(todoId);
        return ResponseEntity.noContent().build();
    }
}

package com.todoList.TodoListWebApp.service.impl;

import com.todoList.TodoListWebApp.dto.TodoCreateDto;
import com.todoList.TodoListWebApp.dto.TodoResponseDto;
import com.todoList.TodoListWebApp.entity.Todo;
import com.todoList.TodoListWebApp.entity.User;
import com.todoList.TodoListWebApp.repository.TodoRepository;
import com.todoList.TodoListWebApp.repository.UserRepository;
import com.todoList.TodoListWebApp.service.TodoService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoServiceImpl implements TodoService {
    private final TodoRepository todoRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    TodoServiceImpl(TodoRepository todoRepository, ModelMapper modelMapper, UserRepository userRepository){
        this.todoRepository = todoRepository;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public TodoResponseDto createNewTodo(Long userId,TodoCreateDto todoCreateDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("user not found"));

        //1st type
//        Todo todo = new Todo();
//        todo.setTitle(todoCreateDto.getTitle());
//        todo.setContent(todoCreateDto.getContent());
//        todo.setUser(user);

        //2nd type
        Todo todo = modelMapper.map(todoCreateDto, Todo.class);
        todo.setUser(user);

        Todo savedTodo = todoRepository.save(todo);
        return modelMapper.map(savedTodo, TodoResponseDto.class);
    }

    @Override
    public TodoResponseDto findTodoById(Long todoId) {
        Todo todo = todoRepository.findById(todoId).orElseThrow(() -> new EntityNotFoundException("todo not found "+todoId));
        return modelMapper.map(todo,TodoResponseDto.class);
    }

    @Override
    public List<TodoResponseDto> findAllTodos() {
        List<Todo> todos = todoRepository.findAll();
        List<TodoResponseDto> todoResponseDtos = todos
                .stream()
                .map(todo -> modelMapper.map(todo,TodoResponseDto.class))
                .toList();

        return todoResponseDtos;
    }

    @Transactional
    @Override
    public TodoResponseDto updateTodo(Long todoId, TodoCreateDto todoCreateDto) {
        if (todoCreateDto.getTitle() == null || todoCreateDto.getContent() == null) {
            throw new IllegalArgumentException("PUT requires all fields");
        }
        Todo todo = todoRepository.findById(todoId).orElseThrow(() -> new EntityNotFoundException("todo not found "+todoId));
        modelMapper.map(todoCreateDto, todo);
        return modelMapper.map(todo,TodoResponseDto.class);
    }

    @Transactional
    @Override
    public TodoResponseDto updatePartialTodo(Long todoId, TodoCreateDto todoCreateDto) {
        Todo todo = todoRepository.findById(todoId).orElseThrow(() -> new IllegalArgumentException("todo not found"));

        if(todoCreateDto.getTitle() != null){
            todo.setTitle(todoCreateDto.getTitle());
        }

        if(todoCreateDto.getContent() != null){
            todo.setContent(todoCreateDto.getContent());
        }

        return modelMapper.map(todo, TodoResponseDto.class);
    }

    @Transactional
    @Override
    public void deleteTodoById(Long todoId) {
        Todo todo = todoRepository.findById(todoId).orElseThrow(() -> new IllegalArgumentException("todo not found "+todoId));
        todoRepository.deleteById(todoId);
    }
}

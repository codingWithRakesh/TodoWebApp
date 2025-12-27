package com.todoList.TodoListWebApp.service;

import com.todoList.TodoListWebApp.dto.UserCreateDto;
import com.todoList.TodoListWebApp.dto.UserCreateResponseDto;
import com.todoList.TodoListWebApp.dto.UserLoginDto;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Map;

public interface UserService {

    UserCreateResponseDto createUser(UserCreateDto userCreateDto);

    UserCreateResponseDto loginUser(UserLoginDto userLoginDto);

    UserCreateResponseDto findById(Long id);

    List<UserCreateResponseDto> findAllUsers();

    UserCreateResponseDto updateUserById(Long id, Map<String, Object> updateBody);

    void deleteById(Long id);
}

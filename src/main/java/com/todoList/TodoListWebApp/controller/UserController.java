package com.todoList.TodoListWebApp.controller;

import com.todoList.TodoListWebApp.dto.UserCreateDto;
import com.todoList.TodoListWebApp.dto.UserCreateResponseDto;
import com.todoList.TodoListWebApp.dto.UserLoginDto;
import com.todoList.TodoListWebApp.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/check")
    public ResponseEntity<Map<String, String>> checking(){
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("api","working"));
    }

    @PostMapping("/create")
    public ResponseEntity<UserCreateResponseDto> createUser(@RequestBody UserCreateDto userCreateDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(userCreateDto));
    }

    @PostMapping("/login")
    public ResponseEntity<UserCreateResponseDto> loginUser(@RequestBody UserLoginDto userLoginDto){
        return ResponseEntity.status(HttpStatus.OK).body(userService.loginUser(userLoginDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserCreateResponseDto> getUserById(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(userService.findById(id));
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserCreateResponseDto>> getAllUser(){
        return ResponseEntity.status(HttpStatus.OK).body(userService.findAllUsers());
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<UserCreateResponseDto> editUser(@PathVariable Long id, @RequestBody Map<String, Object> updateBody){
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUserById(id, updateBody));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}

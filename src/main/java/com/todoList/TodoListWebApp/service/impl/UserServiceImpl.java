package com.todoList.TodoListWebApp.service.impl;

import com.todoList.TodoListWebApp.dto.UserCreateDto;
import com.todoList.TodoListWebApp.dto.UserCreateResponseDto;
import com.todoList.TodoListWebApp.dto.UserLoginDto;
import com.todoList.TodoListWebApp.entity.User;
import com.todoList.TodoListWebApp.repository.UserRepository;
import com.todoList.TodoListWebApp.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper){
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public UserCreateResponseDto createUser(UserCreateDto userCreateDto) {
        if (userRepository.existsByEmail(userCreateDto.getEmail())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Email already registered");
        }

        User user = modelMapper.map(userCreateDto, User.class);

        user = userRepository.save(user);
        return modelMapper.map(user, UserCreateResponseDto.class);
    }

    @Override
    public UserCreateResponseDto loginUser(UserLoginDto userLoginDto) {
        User user = userRepository.findByEmail(userLoginDto.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        if(!userLoginDto.getPassword().equals(user.getPassword())){
            throw new IllegalArgumentException("password not match");
        }
        return modelMapper.map(user, UserCreateResponseDto.class);
    }

    @Override
    public UserCreateResponseDto findById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("user not found "+id));
        return modelMapper.map(user, UserCreateResponseDto.class);
    }

    @Override
    public List<UserCreateResponseDto> findAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserCreateResponseDto> userCreateResponseDtos = users
                .stream()
                .map(user -> modelMapper.map(user, UserCreateResponseDto.class))
                .toList();

        return userCreateResponseDtos;
    }

    @Transactional
    @Override
    public UserCreateResponseDto updateUserById(Long id, Map<String, Object> updateBody) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("user not found "+id));

        updateBody.forEach((field, value) -> {
            switch (field) {
                case "name" :
                    user.setName((String) value);
                    break;
                case "email" :
                    user.setEmail((String) value);
                    break;
                default:
                    throw new IllegalArgumentException("not match");
            }
        });

        return modelMapper.map(user, UserCreateResponseDto.class);
    }

    @Override
    public void deleteById(Long id) {
        if(!userRepository.existsById(id)){
            throw new IllegalArgumentException("id not found");
        }
        userRepository.deleteById(id);
    }
}

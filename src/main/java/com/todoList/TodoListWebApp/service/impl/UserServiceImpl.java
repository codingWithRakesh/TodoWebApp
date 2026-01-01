package com.todoList.TodoListWebApp.service.impl;

import com.todoList.TodoListWebApp.dto.LoginResponseDto;
import com.todoList.TodoListWebApp.dto.UserCreateDto;
import com.todoList.TodoListWebApp.dto.UserCreateResponseDto;
import com.todoList.TodoListWebApp.dto.UserLoginDto;
import com.todoList.TodoListWebApp.entity.User;
import com.todoList.TodoListWebApp.repository.UserRepository;
import com.todoList.TodoListWebApp.security.AuthUtil;
import com.todoList.TodoListWebApp.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final AuthUtil authUtill;

    UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, AuthUtil authUtil){
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.authUtill = authUtil;
    }

    @Override
    @Transactional
    public UserCreateResponseDto createUser(UserCreateDto userCreateDto) {
        User user = userRepository.findByEmail(userCreateDto.getEmail()).orElse(null);

        if (user != null){
            throw new IllegalArgumentException("user alredy exist");
        }

        User newUser = User.builder()
                .email(userCreateDto.getEmail())
                .name(userCreateDto.getName())
                .password(passwordEncoder.encode(userCreateDto.getPassword()))
                .build();

        user = userRepository.save(newUser);
        return modelMapper.map(user, UserCreateResponseDto.class);
    }

    @Override
    public LoginResponseDto loginUser(UserLoginDto userLoginDto) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(userLoginDto.getEmail(), userLoginDto.getPassword())
        );

        User user = (User) authentication.getPrincipal();

        String token = authUtill.genrateAccessToken(user);

        return new LoginResponseDto(user.getId(), user.getEmail(), user.getName(), token);
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

package com.cotae.platform.userservice.application;

import com.cotae.platform.userservice.dto.UserDto;
import com.cotae.platform.userservice.dto.UserRegisterResponseDto;

import java.util.concurrent.CompletableFuture;

public interface UserService {
    //User CRUD
    UserDto createNewUser(String email, String password);
    UserRegisterResponseDto createUser(String email, String password);
}
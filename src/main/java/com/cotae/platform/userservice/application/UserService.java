package com.cotae.platform.userservice.application;

import com.cotae.platform.userservice.dto.UserDto;

import java.util.concurrent.CompletableFuture;

public interface UserService {
    //User CRUD
    UserDto createNewUser(String email, String password);
    CompletableFuture<Object> createUser(String email, String password);
}
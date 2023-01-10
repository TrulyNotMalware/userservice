package com.cotae.platform.userservice.aggregate.auth.application;

import com.cotae.platform.userservice.aggregate.auth.dto.UserDto;

public interface UserService {
    //User CRUD
    UserDto createNewUser(String email, String password);
}
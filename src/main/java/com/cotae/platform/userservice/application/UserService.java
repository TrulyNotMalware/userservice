package com.cotae.platform.userservice.application;

import com.cotae.platform.userservice.dto.UserDto;

public interface UserService {
    //User CRUD
    UserDto createNewUser(String email, String password);
}
package com.cotae.platform.userservice.infrastructure;

import com.cotae.platform.userservice.dto.UserDto;

public interface UserInfra {
    //Read
    UserDto selectUserById(Long id);
    UserDto selectUserByEmail(String email);
    boolean isExistsByEmail(String email);

    //Create
    UserDto insertNewUser(String email, String password);
}

package com.cotae.platform.userservice.domain;

import com.cotae.platform.userservice.dto.UserDto;
import org.springframework.security.core.userdetails.User;

public interface UserDomain {
    /**
     * Create User Secrets.
     * @param userName userEmail. ( Id value )
     * @return org.springframework.security.core.userdetails.User
     */
    User createUserSecurity(String userName);

    /**
     * Check email is duplicated
     * @param email userEmail ( Id value )
     * @return boolean (True / False)
     */
    boolean isDuplicatedEmail(String email);

    /**
     * Create New User.
     * @param email userEmail ( userEmail )
     * @param password password
     * @return UserDto Object
     */
    UserDto createNewUser(String email, String password);
}
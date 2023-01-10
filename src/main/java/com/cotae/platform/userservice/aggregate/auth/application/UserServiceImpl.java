package com.cotae.platform.userservice.aggregate.auth.application;

import com.cotae.platform.userservice.aggregate.auth.domain.UserDomain;
import com.cotae.platform.userservice.aggregate.auth.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserDomain userDomain;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userDomain.createUserSecurity(username);
    }

    @Override
    public UserDto createNewUser(String email, String password) {
        //중복가입 방지, Email 여부 확인.
        if(this.userDomain.isDuplicatedEmail(email)){
            throw new RuntimeException("Email Already signed.");
        }
        return this.userDomain.createNewUser(email, password);
    }
}

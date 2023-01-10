package com.cotae.platform.userservice.aggregate.auth.domain;

import com.cotae.platform.userservice.aggregate.auth.dto.UserDto;
import com.cotae.platform.userservice.aggregate.auth.infrastructure.UserInfra;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDomainImpl implements UserDomain{
    private final UserInfra userInfra;

    @Override
    public User createUserSecurity(String userName) {
        //userName -> email 으로 select.
        UserDto users = this.userInfra.selectUserByEmail(userName);

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(users.getDtype()));
        return new User(users.getId().toString(), users.getPassword(), authorities);
    }

    @Override
    public boolean isDuplicatedEmail(String email) {
        return this.userInfra.isExistsByEmail(email);
    }

    @Override
    public UserDto createNewUser(String email, String password) {
        return this.userInfra.insertNewUser(email, password);
    }
}

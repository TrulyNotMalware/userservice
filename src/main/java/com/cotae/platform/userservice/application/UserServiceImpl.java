package com.cotae.platform.userservice.application;

import com.cotae.platform.userservice.commands.UserCreateCommand;
import com.cotae.platform.userservice.domain.UserDomain;
import com.cotae.platform.userservice.dto.UserDto;
import com.cotae.platform.userservice.dto.UserRegisterResponseDto;
import com.cotae.platform.userservice.global.status.ResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserDomain userDomain;
    private final CommandGateway commandGateway;

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
//        String result = this.commandGateway.sendAndWait(new UserCreateCommand(email, password));
//        log.info(result);
        return this.userDomain.createNewUser(email, password);
    }

    @Override
    public UserRegisterResponseDto createUser(String email, String password){
        //중복가입 방지, Email 여부 확인.
        if(this.userDomain.isDuplicatedEmail(email)){
            throw new RuntimeException("Email Already signed.");
        }
        // FIXME Keep in mind that .join() is blocking!
        String authEmail = this.commandGateway.send(new UserCreateCommand(email, password)).join().toString();
        return new UserRegisterResponseDto(authEmail, ResponseStatus.SUCCESSFULLY_WORKED,"Successfully signed.");
    }
}

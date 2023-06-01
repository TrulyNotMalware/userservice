package com.cotae.platform.userservice.infrastructure;

import com.cotae.platform.userservice.dao.UserRepository;
import com.cotae.platform.userservice.dto.UserDto;
import com.cotae.platform.userservice.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserInfraImpl implements UserInfra{
    //JPA Repository
    private final UserRepository userRepository;

    @Override
    public UserDto selectUserById(Long id) {
        UserEntity user = this.userRepository.findById(id).orElseThrow(()
                -> new RuntimeException("User not found."));
        return new UserDto(user.getId(),user.getPassword(),user.getEmail(),user.getDtype());
    }
    @Override
    public UserDto selectUserByEmail(String email) {
        UserEntity user = this.userRepository.findByEmail(email).orElseThrow(()
                -> new RuntimeException("User not found."));
        return new UserDto(user.getId(), user.getPassword(), user.getEmail(), user.getDtype());
    }

    @Override
    public boolean isExistsByEmail(String email) {
        return this.userRepository.existsByEmail(email);
    }


    @Override
    @Transactional
    public UserDto insertNewUser(String email, String password) {
        this.userRepository.save(new UserEntity(email, password));
        return selectUserByEmail(email);
    }
}

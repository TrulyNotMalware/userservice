package com.cotae.platform.userservice.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;

@Entity(name = "users")
@Getter
@NoArgsConstructor
@DiscriminatorColumn(name = "DTYPE")
public class UserEntity {
    @Id
    @GeneratedValue
    @Column(name = "userId")
    private Long id;
    private String email;
    private String password;

    @Column(insertable = false, updatable = false)
    protected String dtype;

    public UserEntity(String email, String password) {
        this.email = email;
        this.password = new BCryptPasswordEncoder().encode(password);
    }
}
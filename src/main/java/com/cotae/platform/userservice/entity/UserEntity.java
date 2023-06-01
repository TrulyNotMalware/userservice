package com.cotae.platform.userservice.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import jakarta.persistence.*;

@Entity(name = "users")
@Getter
@NoArgsConstructor
@DiscriminatorColumn(name = "DTYPE")
@SequenceGenerator(
        name = "USER_SQ_GENERATOR",
        sequenceName = "USER_SEQ",
        initialValue = 1,
        allocationSize = 1
)//Oracle Database - AutoIncrements Support.
public class UserEntity {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "USER_SEQ"
    )
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
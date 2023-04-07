package com.cotae.platform.userservice.aggregate;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Slf4j
@NoArgsConstructor
@Aggregate
public class StateStoreUser {

    @AggregateIdentifier
    @Id
    @GeneratedValue
    @Column(name = "userId")
    private Long id;
    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password;

    @Column(insertable = false, updatable = false)
    protected String dtype;

    public StateStoreUser(String email, String password) {
        this.email = email;
        this.password = new BCryptPasswordEncoder().encode(password);
    }
}

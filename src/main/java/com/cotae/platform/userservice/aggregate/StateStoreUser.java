package com.cotae.platform.userservice.aggregate;

import com.cotae.platform.userservice.commands.UserCreateCommand;
import com.cotae.platform.userservice.events.StateUserCreationEvent;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import jakarta.persistence.*;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;
import static org.axonframework.modelling.command.AggregateLifecycle.markDeleted;

@Slf4j
@NoArgsConstructor
@Aggregate
@DiscriminatorColumn(name = "DTYPE")
@Entity(name = "stateuser")
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

    @CommandHandler
    public StateStoreUser(UserCreateCommand command) {
        log.info("handling {}",command);
        this.email = command.getEmail();
        this.password = new BCryptPasswordEncoder().encode(command.getPassword());

        //Apply to DB.
        apply(new StateUserCreationEvent(this.email, this.password));
    }
}

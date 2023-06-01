package com.cotae.platform.userservice.commands;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
public class UserCreateCommand {
    @TargetAggregateIdentifier
    private String email;
    private String password;
}

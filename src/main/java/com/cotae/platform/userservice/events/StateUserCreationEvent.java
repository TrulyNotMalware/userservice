package com.cotae.platform.userservice.events;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Getter
public class StateUserCreationEvent {
    private String email;
    private String password;
}

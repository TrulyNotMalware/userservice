package com.cotae.platform.userservice.events;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Getter
public class StateUserCreationEvent {
    private Long id;
    private String email;
    private String password;
}

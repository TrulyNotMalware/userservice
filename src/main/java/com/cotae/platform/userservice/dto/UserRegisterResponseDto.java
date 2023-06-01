package com.cotae.platform.userservice.dto;

import com.cotae.platform.userservice.global.status.ResponseStatus;
import com.cotae.platform.userservice.global.status.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserRegisterResponseDto {
    private String authEmail;
    private Status status; //FIXME status info.
    private String messages;
}
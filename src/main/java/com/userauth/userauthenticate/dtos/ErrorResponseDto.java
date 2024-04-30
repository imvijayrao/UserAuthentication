package com.userauth.userauthenticate.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponseDto {
    private int errorCode;
    private String errorMessage;
}

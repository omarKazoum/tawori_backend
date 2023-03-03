package com.tawori.spaceManagment.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RegisterException extends Exception{

    public RegisterException(String message) {
        super(message);
    }
}

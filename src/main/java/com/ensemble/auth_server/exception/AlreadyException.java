package com.ensemble.auth_server.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class AlreadyException extends RuntimeException {

    public AlreadyException(String message) {
        super(message);
    }

}

package com.alex.buscacep.infra.service.exceptions;

public class DuplicatedUserException extends RuntimeException {

    public DuplicatedUserException(String msg) {
        super(msg);
    }
}

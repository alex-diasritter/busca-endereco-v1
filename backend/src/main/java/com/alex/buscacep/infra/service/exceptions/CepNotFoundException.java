package com.alex.buscacep.infra.service.exceptions;

public class CepNotFoundException extends RuntimeException {

    public CepNotFoundException (String s) {
        super(s);
    }
}

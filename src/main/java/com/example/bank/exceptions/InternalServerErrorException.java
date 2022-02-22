package com.example.bank.exceptions;

public class InternalServerErrorException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public InternalServerErrorException(String errorMessage) {
        super("Internal server error - " + errorMessage);
    }

    public InternalServerErrorException(String errorMessage, Throwable e) {
        super("Internal server error - " + errorMessage, e);
    }

}

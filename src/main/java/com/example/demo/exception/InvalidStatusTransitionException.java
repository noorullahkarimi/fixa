package com.example.demo.exception;



public class InvalidStatusTransitionException extends BusinessException {

    public InvalidStatusTransitionException(String message) {
        super(message);
    }
}
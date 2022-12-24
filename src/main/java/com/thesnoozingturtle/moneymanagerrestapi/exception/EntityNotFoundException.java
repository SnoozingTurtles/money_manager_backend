package com.thesnoozingturtle.moneymanagerrestapi.exception;


public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String message) {
        super(message);
    }
}

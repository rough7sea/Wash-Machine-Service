package com.example.washmachine.common.exception;

import lombok.Getter;

@Getter
public class ServiceException extends RuntimeException{

    private final ExceptionId id;
    private Exception causeEx;

    public ServiceException(ExceptionId id, String message, Exception ex){
        super(message);
        this.id = id;
        this.causeEx = ex;
    }

    public ServiceException(ExceptionId id, String message){
        super(message);
        this.id = id;
    }

    @Override
    public String toString() {
        return id + ":" + this.getLocalizedMessage();
    }
}

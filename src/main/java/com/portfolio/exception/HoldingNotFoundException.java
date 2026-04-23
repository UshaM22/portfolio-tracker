package com.portfolio.exception;

public class HoldingNotFoundException extends RuntimeException{

    public HoldingNotFoundException(String message){
        super(message);
    }
}

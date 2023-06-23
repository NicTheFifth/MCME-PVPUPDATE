package com.mcmiddleearth.pvpplugin.exceptions;

public class BadMaxPlayerException extends RuntimeException{
    public BadMaxPlayerException(String errorMessage){
        super(errorMessage);
    }
}

package com.mcmiddleearth.mcme.pvpplugin.exceptions;

public class BadMaxPlayerException extends RuntimeException{
    public BadMaxPlayerException(String errorMessage){
        super(errorMessage);
    }
}

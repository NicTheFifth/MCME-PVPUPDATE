package com.mcmiddleearth.pvpplugin.exceptions;

public class NoPlayerException extends RuntimeException{

    public NoPlayerException(){
        super("Command context has no Player.");
    }
}

package com.mcmiddleearth.pvpplugin.exceptions;

public class UnloadableGamemodeException extends RuntimeException{
    public UnloadableGamemodeException(Exception e) {
        super("Tried to load stat, but failed with: " + e.getMessage());
    }
}

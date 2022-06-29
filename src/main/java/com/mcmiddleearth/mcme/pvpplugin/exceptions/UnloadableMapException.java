package com.mcmiddleearth.mcme.pvpplugin.exceptions;

public class UnloadableMapException extends RuntimeException{
    public UnloadableMapException(Exception e) {
        super("Tried to load map, but failed with: " + e.getMessage());
    }
}

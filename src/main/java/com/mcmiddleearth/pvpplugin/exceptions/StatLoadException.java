package com.mcmiddleearth.pvpplugin.exceptions;

public class StatLoadException extends RuntimeException{
    public StatLoadException(Exception e) {
    super("Tried to load stat, but failed with: " + e.getMessage());
}
}
package com.mcmiddleearth.pvpplugin.exceptions;

public class MapLoadException extends RuntimeException{
    public MapLoadException(Exception e) {
    super("Tried to load maps, but failed with: " + e.getMessage());
}
}

package com.mcmiddleearth.pvpplugin.exceptions;

public class BadRegionException extends RuntimeException{
    public BadRegionException(String mapName) {
        super(String.format("Tried to transcribe the region on '%s'",mapName));
    }
}

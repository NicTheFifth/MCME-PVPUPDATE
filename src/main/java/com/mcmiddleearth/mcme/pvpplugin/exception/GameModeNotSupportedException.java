package com.mcmiddleearth.mcme.pvpplugin.exception;

public class GameModeNotSupportedException extends RuntimeException {

    public GameModeNotSupportedException(String mapName, String gamemode) {
        super(String.format("Tried to start unsupported gamemode '%s' on map '%s'",gamemode,mapName));
    }
}

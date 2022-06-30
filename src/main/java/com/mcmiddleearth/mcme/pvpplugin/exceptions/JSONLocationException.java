package com.mcmiddleearth.mcme.pvpplugin.exceptions;

public class JSONLocationException extends RuntimeException {

    public JSONLocationException(String mapName, String gamemode, String location) {
        super(String.format("Tried to transcribe %s %s on map %s",location,gamemode,mapName));
    }
}

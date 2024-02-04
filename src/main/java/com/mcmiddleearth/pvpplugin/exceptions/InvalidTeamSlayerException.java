package com.mcmiddleearth.pvpplugin.exceptions;

import com.mcmiddleearth.pvpplugin.json.jsonData.JSONMap;

public class InvalidTeamSlayerException extends RuntimeException{
    public InvalidTeamSlayerException(JSONMap map){
        super(String.format("TeamSlayer on %s isn't initialised correctly, " +
            "please choose another map.",map.getTitle()));
    }
}

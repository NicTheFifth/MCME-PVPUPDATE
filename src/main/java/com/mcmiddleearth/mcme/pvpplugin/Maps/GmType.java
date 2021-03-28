package com.mcmiddleearth.mcme.pvpplugin.Maps;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

public class GmType {

    @Getter
    @Setter
    private Gamemode gm;

    @Getter
    @Setter
    private int maxPlayers;

    @Getter
    @Setter
    private HashMap<String, Event> events = new HashMap<>();
    /*
    GmTypes 		(list of gamemodes on map)
        Type 			(type of gamemode)
        Max		 	(max players
        Important points	(points with no changing of world)
            Spawns 		(spawn points)
            Goals 			(goal points)
        Events			(Events on map)*/
}

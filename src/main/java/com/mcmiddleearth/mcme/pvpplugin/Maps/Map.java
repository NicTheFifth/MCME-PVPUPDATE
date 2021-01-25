package com.mcmiddleearth.mcme.pvpplugin.Maps;

import com.mcmiddleearth.mcme.pvpplugin.Util.EventLocation;
import lombok.Setter;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;

public class Map {
    //TODO: Make the map system

    @Getter @Setter
    private String mapTitle;

    @Getter @Setter
    private EventLocation spawn;

    @Getter @Setter
    private String rp;

    @Getter @Setter
    private ArrayList<EventLocation> regionPoints = new ArrayList<>();

    @Getter @Setter
    private HashMap<String,GmType> gamemodes = new HashMap<>();
    /*
    ShortName 		(abbreviation used to start map)
    Title 			(shown when starting)
    Spawn 		(place where spectators spawn and where you tp to map)
    URL 			(gives rp url)
    RegionPoints		(sets the area)
    GmTypes 		(list of gamemodes on map)*/
}

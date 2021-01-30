package com.mcmiddleearth.mcme.pvpplugin.Maps;

import com.mcmiddleearth.mcme.pvpplugin.Util.EventLocation;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.math.BlockVector2;
import com.sk89q.worldedit.regions.Polygonal2DRegion;
import com.sk89q.worldedit.regions.Region;
import lombok.Setter;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.HashMap;

public class Map {
    //TODO: Make the map system

    public enum gameState { IDLE, COUNTDOWN, RUNNING}

    @Getter @Setter
    private String mapTitle;
    @Getter @Setter
    private EventLocation spawn;
    @Getter @Setter
    private gameState gState;
    @Getter @Setter
    private String rp;
    @Getter @Setter
    private ArrayList<EventLocation> regionPoints = new ArrayList<>();
    @Getter
    private Region region;
    @Getter @Setter
    private HashMap<String,GmType> gamemodes = new HashMap<>();

    public Map() {gState = gameState.IDLE;}

    public void setRegion(){
        ArrayList<BlockVector2> wePoints = new ArrayList<>();
        World world = Bukkit.getWorld("world");
        for(EventLocation e : regionPoints){
            BlockVector2 point = BlockVector2.at(e.getX(), e.getZ());
            wePoints.add(point);
        }
        region = new Polygonal2DRegion(new BukkitWorld(world), wePoints, 0, 1000);
    }

    /*
    ShortName 		(abbreviation used to start map)
    Title 			(shown when starting)
    Spawn 		(place where spectators spawn and where you tp to map)
    URL 			(gives rp url)
    RegionPoints		(sets the area)
    GmTypes 		(list of gamemodes on map)*/
}

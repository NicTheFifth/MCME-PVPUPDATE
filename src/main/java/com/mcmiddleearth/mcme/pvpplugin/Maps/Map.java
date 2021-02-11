package com.mcmiddleearth.mcme.pvpplugin.Maps;

import com.google.common.base.Equivalence;
import com.mcmiddleearth.mcme.pvpplugin.Gamemodes.Gamemode;
import com.mcmiddleearth.mcme.pvpplugin.Util.EventLocation;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.math.BlockVector2;
import com.sk89q.worldedit.regions.Polygonal2DRegion;
import com.sk89q.worldedit.regions.Region;
import lombok.Setter;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Map {
    @Getter@Setter
    public static HashMap<String, Map> maps = new HashMap<>();

    @Getter@Setter
    private HashMap<String, EventLocation> ImportantPoints = new HashMap<>();

    @Getter@Setter
    private Gamemode gm;

    @Getter@Setter
    private String name;

    @Getter@Setter
    private String gmType;

    @Getter @Setter
    private String mapTitle;

    @Getter @Setter
    private EventLocation spawn;

    @Getter @Setter
    private String rp;

    @Getter@Setter
    private String resourcePackURL;

    @Getter@Setter
    private int Max;

    @Getter @Setter
    private ArrayList<EventLocation> regionPoints = new ArrayList<>();

    @Getter
    private Region region;

    @Getter @Setter
    private HashMap<String,GmType> gamemode = new HashMap<>();

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

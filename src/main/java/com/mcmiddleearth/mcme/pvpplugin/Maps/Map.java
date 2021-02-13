package com.mcmiddleearth.mcme.pvpplugin.Maps;

import com.mcmiddleearth.mcme.pvpplugin.json.JSONLocation;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.math.BlockVector2;
import com.sk89q.worldedit.regions.Polygonal2DRegion;
import com.sk89q.worldedit.regions.Region;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.HashMap;

public class Map {
    @Getter
    @Setter
    private String mapTitle;
    @Getter
    @Setter
    private JSONLocation spawn;
    @Getter @Setter
    private Boolean running;
    @Getter @Setter
    private String rp;
    @Getter @Setter
    private ArrayList<JSONLocation> regionPoints = new ArrayList<>();
    @Getter
    private Region region;
    @Getter @Setter
    private HashMap<String,GmType> gamemodes = new HashMap<>();

    public Map() {running = false;}

    public void setRegion(){
        ArrayList<BlockVector2> wePoints = new ArrayList<>();
        World world = Bukkit.getWorld("world");
        for(JSONLocation e : regionPoints){
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

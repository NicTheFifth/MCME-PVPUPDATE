package com.mcmiddleearth.mcme.pvpplugin.json;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
@Getter
@Setter
public class JSONLocation {

    private String world;
    private Integer x;
    private Integer y;
    private Integer z;

    public JSONLocation toJSONLocation(Location location){
        JSONLocation returnLoc = new JSONLocation();
        returnLoc.x = location.getBlockX();
        returnLoc.y = location.getBlockY();
        returnLoc.z = location.getBlockZ();
        returnLoc.world = location.getWorld().getName();
        return returnLoc;
    }

    public Location toBukkitLoc(){
        return new Location(Bukkit.getWorld(world), x, y, z);
    }
}

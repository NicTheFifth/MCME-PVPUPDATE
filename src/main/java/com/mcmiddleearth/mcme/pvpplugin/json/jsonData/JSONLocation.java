package com.mcmiddleearth.mcme.pvpplugin.json.jsonData;

import org.bukkit.Location;

public class JSONLocation {
    Integer x;
    Integer y;
    Integer z;
    String world;

    public JSONLocation(){}

    public JSONLocation(Location loc){
        this.x = loc.getBlockX();
        this.y = loc.getBlockY();
        this.z = loc.getBlockZ();
        this.world = loc.getWorld().getName();
    }

    //<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public Integer getX() {
        return this.x;
    }
    public void setX(final Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return this.y;
    }
    public void setY(final Integer y) {
        this.y = y;
    }

    public Integer getZ() {
        return this.z;
    }
    public void setZ(final Integer z) {
        this.z = z;
    }

    public String getWorld() {
        return this.world;
    }
    public void setWorld(final String world) {
        this.world = world;
    }
    //</editor-fold>
}

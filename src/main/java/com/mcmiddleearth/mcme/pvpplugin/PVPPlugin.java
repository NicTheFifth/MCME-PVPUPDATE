package com.mcmiddleearth.mcme.pvpplugin;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.plugin.java.JavaPlugin;

public class PVPPlugin extends JavaPlugin{

    private static PVPPlugin plugin;

    @Override
    public void onEnable(){
        Logger.getLogger("BasePlugin").log(Level.INFO,"BasePlugin loaded correctly");
        plugin = this;
    }
    @Override
    public void onDisable(){

    }
}

package com.mcmiddleearth.mcme.pvpplugin.Gamemodes;

import com.google.common.collect.Lists;
import com.mcmiddleearth.mcme.pvpplugin.Gamemodes.helpers.BaseGamemode;
import com.mcmiddleearth.mcme.pvpplugin.Maps.Map;
import com.mcmiddleearth.mcme.pvpplugin.PVP.Team;
import com.mcmiddleearth.mcme.pvpplugin.PVPPlugin;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TeamDeathMatch extends BaseGamemode {

    @Getter
    private final List<String> neededPoints = new ArrayList<>(Lists.newArrayList("BlueSpawn", "RedSpawn"));
    private PVPPlugin pvpPlugin;
    private final Team blue = new Team("Blue", ChatColor.BLUE);
    private final Team red = new Team("Red", ChatColor.RED);

    @Override
    public void Start(Map m, PVPPlugin plugin){
        pvpPlugin = plugin;
    }

    @Override
    public void End(){
        blue.clear();
        red.clear();
        super.End();
    }

    @Override
    public void handleEvent(Object event) {

    }

    @Override
    public void onPlayerJoin(Player p) {

    }

    @Override
    public void onPlayerLeave(Player p) {

    }
}

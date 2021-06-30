package com.mcmiddleearth.mcme.pvpplugin.util;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class Team {
    @Getter@Setter
    Set<Player> members = Collections.emptySet();

    @Getter@Setter
    Set<Player> deadMembers = Collections.emptySet();

    @Getter@Setter
    Color teamColour;

    @Getter@Setter
    String Prefix;

    @Getter@Setter
    Kit kit;

    @Getter@Setter
    List<Location> spawnLocations;
}

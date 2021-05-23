package com.mcmiddleearth.mcme.pvpplugin.jsonData.jsonGamemodes;

import com.mcmiddleearth.mcme.pvpplugin.jsonData.JSONLocation;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class FreeForAll {
    @Getter@Setter
    List<JSONLocation> spawns;

    @Getter@Setter
    Integer maximumPlayers;
}

package com.mcmiddleearth.mcme.pvpplugin.json.jsonData.jsonGamemodes;

import com.mcmiddleearth.mcme.pvpplugin.json.jsonData.JSONLocation;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class JSONFreeForAll {
    @Getter@Setter
    List<JSONLocation> spawns;

    @Getter@Setter
    Integer maximumPlayers;
}

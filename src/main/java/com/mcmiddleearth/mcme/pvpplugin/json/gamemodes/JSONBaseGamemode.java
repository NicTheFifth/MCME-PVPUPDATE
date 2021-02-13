package com.mcmiddleearth.mcme.pvpplugin.json.gamemodes;

import com.mcmiddleearth.mcme.pvpplugin.json.JSONEvent;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JSONBaseGamemode {
    private Integer maxPlayers;
    private List<JSONEvent> events;
}

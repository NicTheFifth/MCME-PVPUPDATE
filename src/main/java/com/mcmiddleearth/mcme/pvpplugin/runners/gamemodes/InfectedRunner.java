package com.mcmiddleearth.mcme.pvpplugin.runners.gamemodes;

import com.mcmiddleearth.mcme.pvpplugin.PVPPlugin;
import com.mcmiddleearth.mcme.pvpplugin.json.jsonData.JSONMap;
import com.mcmiddleearth.mcme.pvpplugin.json.transcribers.InfectedTranscriber;
import com.mcmiddleearth.mcme.pvpplugin.util.Kit;
import com.mcmiddleearth.mcme.pvpplugin.util.Team;
import lombok.Getter;
import org.bukkit.Color;

public class InfectedRunner extends BaseRunner {

    @Getter
    Team infected = new Team();
    @Getter
    Team survivors = new Team();

    public InfectedRunner(JSONMap map, PVPPlugin pvpplugin){
        this.pvpPlugin = pvpplugin;
        InfectedTranscriber transcriber = new InfectedTranscriber();
        transcriber.Transcribe(map, this);
        InitialiseInfected();
        InitialiseSurvivors();
    }

    private void InitialiseInfected() {
        infected.setPrefix("Infected");
        infected.setTeamColour(Color.RED);
        infected.setKit(InfectedKit());
    }

    private Kit InfectedKit() {
        return null;
    }

    private void InitialiseSurvivors() {
        survivors.setPrefix("Survivor");
        survivors.setTeamColour(Color.BLUE);
        survivors.setKit(SurvivorKit());
    }

    private Kit SurvivorKit() {
        return null;
    }
}

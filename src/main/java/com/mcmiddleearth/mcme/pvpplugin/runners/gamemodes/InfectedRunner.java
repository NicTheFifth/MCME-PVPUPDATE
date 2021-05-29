package com.mcmiddleearth.mcme.pvpplugin.runners.gamemodes;

import com.mcmiddleearth.mcme.pvpplugin.PVPPlugin;
import com.mcmiddleearth.mcme.pvpplugin.json.jsonData.JSONMap;
import com.mcmiddleearth.mcme.pvpplugin.json.transcribers.InfectedTranscriber;
import com.mcmiddleearth.mcme.pvpplugin.util.Kit;
import com.mcmiddleearth.mcme.pvpplugin.util.Team;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Color;
import org.bukkit.entity.Player;

public class InfectedRunner extends BaseRunner {

    @Getter
    Team infected = new Team();
    @Getter
    Team survivors = new Team();
    @Getter@Setter
    Integer timeMin;
    public InfectedRunner(JSONMap map, PVPPlugin pvpplugin){
        this.pvpPlugin = pvpplugin;
        InfectedTranscriber transcriber = new InfectedTranscriber();
        transcriber.Transcribe(map, this);
        InitialiseInfected();
        InitialiseSurvivors();
    }

    @Override
    public void Start(){

    }

    @Override
    public boolean CanStart(){
        return timeMin != null && super.CanStart();
    }

    @Override
    public void End(){

    }

    @Override
    public boolean CanJoin(Player player){
        return false;
    }

    @Override
    public void Join(Player player){

    }

    @Override
    public void Leave(Player player){

    }

    private void InitialiseInfected() {
        infected.setPrefix("Infected");
        infected.setTeamColour(Color.RED);
        infected.setKit(InfectedKit());
    }
    //TODO: Create infected Kit
    private Kit InfectedKit() {
        return null;
    }

    private void InitialiseSurvivors() {
        survivors.setPrefix("Survivor");
        survivors.setTeamColour(Color.BLUE);
        survivors.setKit(SurvivorKit());
    }
    //TODO: Create survivor Kit
    private Kit SurvivorKit() {
        return null;
    }
}

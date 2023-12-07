package com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor;

import com.mcmiddleearth.command.Style;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONLocation;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONMap;
import com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes.JSONInfected;
import org.bukkit.Location;

import java.util.List;

public class InfectedEditor implements GamemodeEditor{
    JSONInfected jsonInfected;
    private InfectedEditor(){}
    public InfectedEditor(JSONMap map){this.jsonInfected = map.getJSONInfected();
    }
    public List<String> setInfectedSpawn(Location infectedSpawn){
        JSONLocation JSONInfectedSpawn = new JSONLocation(infectedSpawn);
        jsonInfected.setInfectedSpawn(JSONInfectedSpawn);
        return List.of(Style.INFO + "Infected spawn set for Infected.");
    }
    public List<String> setSurvivorSpawn(Location survivorSpawn){
        JSONLocation JSONSurvivorSpawn = new JSONLocation(survivorSpawn);
        jsonInfected.setSurvivorSpawn(JSONSurvivorSpawn);
        return List.of(Style.INFO + "Survivor spawn set for Infected.");
    }
    public String[] setMaxPlayers(Integer maxPlayers) {
        jsonInfected.setMaximumPlayers(maxPlayers);
        return new String[]{String.format(Style.INFO + "Set the max players to %d.", maxPlayers)};
    }
    public String getGamemode(){return "Infected";}
    @Override
    public String[] getInfo(){
        return new String[]{
                String.format(Style.INFO + "Current selected gamemode: Infected."),
                String.format(Style.INFO + "Max players: %d", jsonInfected.getMaximumPlayers()),
                String.format(Style.INFO + "Survivor spawn set: %b", jsonInfected.getSurvivorSpawn()),
                String.format(Style.INFO + "Infected spawn set: %b", jsonInfected.getInfectedSpawn())
        };
    }
}

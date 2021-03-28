package com.mcmiddleearth.mcme.pvpplugin.PVP;

import com.mcmiddleearth.mcme.pvpplugin.PVPPlugin;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Matchmaker {

    public enum ADDITION_TYPE {
        SPAWN,
        NONE
    }

    private PVPPlugin pvpPlugin;

    public Matchmaker(PVPPlugin plugin){
        pvpPlugin = plugin;
    }

    public ArrayList<Set<Player>> makeTeams(Set<Player> players, Team... teams){
        return null;
    }

    public Team addPlayer (ADDITION_TYPE addition_type, Player player, Team... teams){
        return null;
    }
    //matchmaker could go in here
    //for balance reasons
    //         playerList.stream().sorted((player1,player2) ->
    //                                      (Integer.compare(player1.getPvpSkill(), player2.getPvpSkill())))
    //                  .forEachOrdered(player -> {
    //            if(team1.getTotalSkill()<team2.getTotalSkill()) {
    //                team1.add(player);
    //            } else {
    //                team2.add(player);
    //            }
    //        }); -Eriol recommends we can alter it a bit
}
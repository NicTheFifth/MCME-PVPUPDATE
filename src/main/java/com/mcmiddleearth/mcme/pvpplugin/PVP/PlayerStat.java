package com.mcmiddleearth.mcme.pvpplugin.PVP;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Donovan <dallen@dallen.xyz>
 */
@Getter @Setter
public class PlayerStat {

    private int kills = 0;
    private int deaths = 0;
    private int gamesPlayed = 0;
    private int gamesWon = 0;
    private int gamesLost = 0;
    private int gamesSpectated = 0;

    public void addDeath(){
        deaths++;
    }

    public void addKill(){
        kills++;
    }

    public void addPlayedGame(){
        gamesPlayed++;
    }

    public void addGameWon(){
        gamesWon++;
    }

    public void addGameLost(){
        gamesLost++;
    }

    public void addGameSpectated(){
        gamesSpectated++;
    }
}

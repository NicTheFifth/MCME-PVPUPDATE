package com.mcmiddleearth.mcme.pvpplugin.PVP;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Donovan <dallen@dallen.xyz>
 */
public class PlayerStat {
    @Getter @Setter
    private int Kills = 0;
    @Getter @Setter
    private int Deaths = 0;
    @Getter @Setter
    private int gamesPlayed = 0;
    @Getter @Setter
    private int gamesWon = 0;
    @Getter @Setter
    private int gamesLost = 0;
    @Getter @Setter
    private int gamesSpectated = 0;

    public void addDeath(){Deaths++;}
    public void addKill(){Kills++;}
    public void addPlayedGame(){gamesPlayed++;}
    public void addGameWon(){gamesWon++;}
    public void addGameLost(){gamesLost++;};
    public void addGameSpectated(){gamesSpectated++;};
}

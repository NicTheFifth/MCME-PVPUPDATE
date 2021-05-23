package com.mcmiddleearth.mcme.pvpplugin.jsonData;

import lombok.Getter;
import lombok.Setter;

public class Playerstat {
    @Getter@Setter
    Integer deaths;

    @Getter@Setter
    Integer kills;

    @Getter@Setter
    Integer gamesPlayed;

    @Getter@Setter
    Integer gamesWon;

    @Getter@Setter
    Integer gamesLost;

    @Getter@Setter
    Integer gamesSpectated;

    @Getter@Setter
    Integer damageTaken;

    @Getter@Setter
    Integer damageGiven;

    @Getter@Setter
    Integer elo;

    public void addDeath(){
        deaths++;
    }

    public void addKill(){
        kills++;
    }

    public void addPlayed(){
        gamesPlayed++;
    }

    public void addLost(){
        gamesLost++;
    }

    public void addWon(){
        gamesWon++;
    }

    public void addSpectate(){
        gamesSpectated++;
    }

    public void addDamageGiven(Integer damage){
        damageGiven += damage;
    }

    public void addDamageTaken(Integer damage){
        damageTaken += damage;
    }
}

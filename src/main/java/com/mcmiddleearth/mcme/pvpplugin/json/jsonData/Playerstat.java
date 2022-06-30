package com.mcmiddleearth.mcme.pvpplugin.json.jsonData;

public class Playerstat {
    Integer deaths = 0;
    Integer kills = 0;
    Integer gamesPlayed = 0;
    Integer gamesWon = 0;
    Integer gamesLost = 0;
    Integer gamesSpectated = 0;
    Integer damageTaken = 0;
    Integer damageGiven = 0;
    Integer elo = 0;

    public void addDeath() {
        deaths++;
    }

    public void addKill() {
        kills++;
    }

    public void addPlayed() {
        gamesPlayed++;
    }

    public void addLost() {
        gamesLost++;
    }

    public void addWon() {
        gamesWon++;
    }

    public void addSpectate() {
        gamesSpectated++;
    }

    public void addDamageGiven(Integer damage) {
        damageGiven += damage;
    }

    public void addDamageTaken(Integer damage) {
        damageTaken += damage;
    }

    //<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public Integer getDeaths() {
        return this.deaths;
    }
    public void setDeaths(final Integer deaths) {
        this.deaths = deaths;
    }

    public Integer getKills() {
        return this.kills;
    }
    public void setKills(final Integer kills) {
        this.kills = kills;
    }

    public Integer getGamesPlayed() {
        return this.gamesPlayed;
    }
    public void setGamesPlayed(final Integer gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

    public Integer getGamesWon() {
        return this.gamesWon;
    }
    public void setGamesWon(final Integer gamesWon) {
        this.gamesWon = gamesWon;
    }

    public Integer getGamesLost() {
        return this.gamesLost;
    }
    public void setGamesLost(final Integer gamesLost) {
        this.gamesLost = gamesLost;
    }

    public Integer getGamesSpectated() {
        return this.gamesSpectated;
    }
    public void setGamesSpectated(final Integer gamesSpectated) {
        this.gamesSpectated = gamesSpectated;
    }

    public Integer getDamageTaken() {
        return this.damageTaken;
    }
    public void setDamageTaken(final Integer damageTaken) {
        this.damageTaken = damageTaken;
    }

    public Integer getDamageGiven() {
        return this.damageGiven;
    }
    public void setDamageGiven(final Integer damageGiven) {
        this.damageGiven = damageGiven;
    }

    public Integer getElo() {
        return this.elo;
    }
    public void setElo(final Integer elo) {
        this.elo = elo;
    }
    //</editor-fold>
}

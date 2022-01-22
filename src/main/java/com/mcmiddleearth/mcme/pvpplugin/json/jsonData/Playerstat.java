package com.mcmiddleearth.mcme.pvpplugin.json.jsonData;

public class Playerstat {
    Integer deaths;
    Integer kills;
    Integer gamesPlayed;
    Integer gamesWon;
    Integer gamesLost;
    Integer gamesSpectated;
    Integer damageTaken;
    Integer damageGiven;
    Integer elo;

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

    //<editor-fold defaultstate="collapsed" desc="delombok">
    @SuppressWarnings("all")
    public Integer getDeaths() {
        return this.deaths;
    }

    @SuppressWarnings("all")
    public void setDeaths(final Integer deaths) {
        this.deaths = deaths;
    }

    @SuppressWarnings("all")
    public Integer getKills() {
        return this.kills;
    }

    @SuppressWarnings("all")
    public void setKills(final Integer kills) {
        this.kills = kills;
    }

    @SuppressWarnings("all")
    public Integer getGamesPlayed() {
        return this.gamesPlayed;
    }

    @SuppressWarnings("all")
    public void setGamesPlayed(final Integer gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

    @SuppressWarnings("all")
    public Integer getGamesWon() {
        return this.gamesWon;
    }

    @SuppressWarnings("all")
    public void setGamesWon(final Integer gamesWon) {
        this.gamesWon = gamesWon;
    }

    @SuppressWarnings("all")
    public Integer getGamesLost() {
        return this.gamesLost;
    }

    @SuppressWarnings("all")
    public void setGamesLost(final Integer gamesLost) {
        this.gamesLost = gamesLost;
    }

    @SuppressWarnings("all")
    public Integer getGamesSpectated() {
        return this.gamesSpectated;
    }

    @SuppressWarnings("all")
    public void setGamesSpectated(final Integer gamesSpectated) {
        this.gamesSpectated = gamesSpectated;
    }

    @SuppressWarnings("all")
    public Integer getDamageTaken() {
        return this.damageTaken;
    }

    @SuppressWarnings("all")
    public void setDamageTaken(final Integer damageTaken) {
        this.damageTaken = damageTaken;
    }

    @SuppressWarnings("all")
    public Integer getDamageGiven() {
        return this.damageGiven;
    }

    @SuppressWarnings("all")
    public void setDamageGiven(final Integer damageGiven) {
        this.damageGiven = damageGiven;
    }

    @SuppressWarnings("all")
    public Integer getElo() {
        return this.elo;
    }

    @SuppressWarnings("all")
    public void setElo(final Integer elo) {
        this.elo = elo;
    }
    //</editor-fold>
}

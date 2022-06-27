package com.mcmiddleearth.mcme.pvpplugin.json.jsonData;

import com.mcmiddleearth.mcme.pvpplugin.json.jsonData.jsonGamemodes.*;

import java.util.List;

public class JSONMap {
    List<JSONLocation> regionPoints;

    String title;

    JSONLocation spawn;

    String resourcePack;

    JSONCaptureTheFlag JSONCaptureTheFlag = new JSONCaptureTheFlag();

    JSONDeathRun JSONDeathRun = new JSONDeathRun();

    JSONFreeForAll JSONFreeForAll = new JSONFreeForAll();

    JSONInfected JSONInfected = new JSONInfected();

    JSONTeamConquest JSONTeamConquest = new JSONTeamConquest();

    JSONTeamDeathMatch JSONTeamDeathMatch = new JSONTeamDeathMatch();

    JSONTeamSlayer JSONTeamSlayer = new JSONTeamSlayer();

    public JSONMap(){}
    public JSONMap(String title) {
        this.title = title;
    }

    //<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public List<JSONLocation> getRegionPoints() {
        return this.regionPoints;
    }
    public void setRegionPoints(List<JSONLocation> regionPoints) {
        this.regionPoints = regionPoints;
    }

    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public JSONLocation getSpawn() {
        return this.spawn;
    }
    public void setSpawn(JSONLocation spawn) {
        this.spawn = spawn;
    }

    public String getResourcePack() {
        return this.resourcePack;
    }
    public void setResourcePack(String resourcePack) {
        this.resourcePack = resourcePack;
    }

    public JSONCaptureTheFlag getJSONCaptureTheFlag() {
        return this.JSONCaptureTheFlag;
    }
    public void setJSONCaptureTheFlag(JSONCaptureTheFlag JSONCaptureTheFlag) {
        this.JSONCaptureTheFlag = JSONCaptureTheFlag;
    }

    public JSONDeathRun getJSONDeathRun() {
        return this.JSONDeathRun;
    }
    public void setJSONDeathRun(JSONDeathRun JSONDeathRun) {
        this.JSONDeathRun = JSONDeathRun;
    }

    public JSONFreeForAll getJSONFreeForAll() {
        return this.JSONFreeForAll;
    }
    public void setJSONFreeForAll(JSONFreeForAll JSONFreeForAll) {
        this.JSONFreeForAll = JSONFreeForAll;
    }

    public JSONInfected getJSONInfected() {
        return this.JSONInfected;
    }
    public void setJSONInfected(JSONInfected JSONInfected) {
        this.JSONInfected = JSONInfected;
    }

    public JSONTeamConquest getJSONTeamConquest() {
        return this.JSONTeamConquest;
    }
    public void setJSONTeamConquest(JSONTeamConquest JSONTeamConquest) {
        this.JSONTeamConquest = JSONTeamConquest;
    }

    public JSONTeamDeathMatch getJSONTeamDeathMatch() {
        return this.JSONTeamDeathMatch;
    }
    public void setJSONTeamDeathMatch(JSONTeamDeathMatch JSONTeamDeathMatch) {
        this.JSONTeamDeathMatch = JSONTeamDeathMatch;
    }

    public JSONTeamSlayer getJSONTeamSlayer() {
        return this.JSONTeamSlayer;
    }
    public void setJSONTeamSlayer(JSONTeamSlayer JSONTeamSlayer) {
        this.JSONTeamSlayer = JSONTeamSlayer;
    }
    //</editor-fold>
}
package com.mcmiddleearth.mcme.pvpplugin.json.jsonData;

import com.mcmiddleearth.mcme.pvpplugin.json.jsonData.jsonGamemodes.*;

import java.util.List;

public class JSONMap {
    List<JSONLocation> regionPoints;

    String title;

    JSONLocation spawn;

    String resourcePack;

    JSONCaptureTheFlag JSONCaptureTheFlag;

    JSONDeathRun JSONDeathRun;

    JSONFreeForAll JSONFreeForAll;

    JSONInfected JSONInfected;

    JSONTeamConquest JSONTeamConquest;

    JSONTeamDeathMatch JSONTeamDeathMatch;

    JSONTeamSlayer JSONTeamSlayer;

    public List<JSONLocation> getRegionPoints() {
        return this.regionPoints;
    }

    public String getTitle() {
        return this.title;
    }

    public JSONLocation getSpawn() {
        return this.spawn;
    }

    public String getResourcePack() {
        return this.resourcePack;
    }

    public JSONCaptureTheFlag getJSONCaptureTheFlag() {
        return this.JSONCaptureTheFlag;
    }

    public JSONDeathRun getJSONDeathRun() {
        return this.JSONDeathRun;
    }

    public JSONFreeForAll getJSONFreeForAll() {
        return this.JSONFreeForAll;
    }

    public JSONInfected getJSONInfected() {
        return this.JSONInfected;
    }

    public JSONTeamConquest getJSONTeamConquest() {
        return this.JSONTeamConquest;
    }

    public JSONTeamDeathMatch getJSONTeamDeathMatch() {
        return this.JSONTeamDeathMatch;
    }

    public JSONTeamSlayer getJSONTeamSlayer() {
        return this.JSONTeamSlayer;
    }

    public void setRegionPoints(List<JSONLocation> regionPoints) {
        this.regionPoints = regionPoints;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSpawn(JSONLocation spawn) {
        this.spawn = spawn;
    }

    public void setResourcePack(String resourcePack) {
        this.resourcePack = resourcePack;
    }

    public void setJSONCaptureTheFlag(JSONCaptureTheFlag JSONCaptureTheFlag) {
        this.JSONCaptureTheFlag = JSONCaptureTheFlag;
    }

    public void setJSONDeathRun(JSONDeathRun JSONDeathRun) {
        this.JSONDeathRun = JSONDeathRun;
    }

    public void setJSONFreeForAll(JSONFreeForAll JSONFreeForAll) {
        this.JSONFreeForAll = JSONFreeForAll;
    }

    public void setJSONInfected(JSONInfected JSONInfected) {
        this.JSONInfected = JSONInfected;
    }

    public void setJSONTeamConquest(JSONTeamConquest JSONTeamConquest) {
        this.JSONTeamConquest = JSONTeamConquest;
    }

    public void setJSONTeamDeathMatch(JSONTeamDeathMatch JSONTeamDeathMatch) {
        this.JSONTeamDeathMatch = JSONTeamDeathMatch;
    }

    public void setJSONTeamSlayer(JSONTeamSlayer JSONTeamSlayer) {
        this.JSONTeamSlayer = JSONTeamSlayer;
    }
}
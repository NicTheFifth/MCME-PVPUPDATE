package com.mcmiddleearth.pvpplugin.json.jsonData;

import com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes.*;

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

    JSONOneInTheQuiver JSONOneInTheQuiver;

    JSONRingBearer JSONRingBearer;

    JSONTeamConquest JSONTeamConquest;

    JSONTeamDeathMatch JSONTeamDeathMatch;

    JSONTeamSlayer JSONTeamSlayer;

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

    public JSONOneInTheQuiver getJSONOneInTheQuiver() {
        return JSONOneInTheQuiver;
    }
    public void setJSONOneInTheQuiver(JSONOneInTheQuiver JSONOneInTheQuiver) {
        this.JSONOneInTheQuiver = JSONOneInTheQuiver;
    }

    public JSONRingBearer getJSONRingBearer() {
        return JSONRingBearer;
    }
    public void setJSONRingBearer(JSONRingBearer JSONRingBearer) {
        this.JSONRingBearer = JSONRingBearer;
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
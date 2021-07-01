package com.mcmiddleearth.mcme.pvpplugin.json.jsonData;

import com.mcmiddleearth.mcme.pvpplugin.json.jsonData.jsonGamemodes.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class JSONMap {
    @Getter@Setter
    List<JSONLocation> regionPoints;

    @Getter@Setter
    String title;

    @Getter@Setter
    JSONLocation spawn;

    @Getter@Setter
    String resourcePack;

    @Getter@Setter
    String abbreviation;

    @Getter@Setter
    JSONCaptureTheFlag JSONCaptureTheFlag;

    @Getter@Setter
    JSONDeathRun JSONDeathRun;

    @Getter@Setter
    JSONFreeForAll JSONFreeForAll;

    @Getter@Setter
    JSONInfected JSONInfected;

    @Getter@Setter
    JSONTeamConquest JSONTeamConquest;

    @Getter@Setter
    JSONTeamDeathMatch JSONTeamDeathMatch;

    @Getter@Setter
    JSONTeamSlayer JSONTeamSlayer;
}
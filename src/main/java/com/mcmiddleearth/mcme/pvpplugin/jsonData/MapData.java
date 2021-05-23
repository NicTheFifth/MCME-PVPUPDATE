package com.mcmiddleearth.mcme.pvpplugin.jsonData;

import com.mcmiddleearth.mcme.pvpplugin.jsonData.jsonGamemodes.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class MapData {
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
    CaptureTheFlag captureTheFlag;

    @Getter@Setter
    DeathRun deathRun;

    @Getter@Setter
    FreeForAll freeForAll;

    @Getter@Setter
    Infected infected;

    @Getter@Setter
    TeamConquest teamConquest;

    @Getter@Setter
    TeamDeathMatch teamDeathMatch;

    @Getter@Setter
    TeamSlayer teamSlayer;
}

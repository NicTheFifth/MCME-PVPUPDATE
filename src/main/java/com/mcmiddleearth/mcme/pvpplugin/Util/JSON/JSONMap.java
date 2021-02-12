package com.mcmiddleearth.mcme.pvpplugin.Util.JSON;

import com.mcmiddleearth.mcme.pvpplugin.Util.JSON.Gamemodes.JSONGamemode;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;

public class JSONMap{
        @Getter
        private String mapTitle;
        @Getter
        private JSONLocation spawn;
        @Getter
        private String rp;
        @Getter
        private ArrayList<JSONLocation> regionPoints;
        @Getter
        private HashMap<String, JSONGamemode> gamemodes;
}

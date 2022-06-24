package com.mcmiddleearth.mcme.pvpplugin.util;

import com.mcmiddleearth.mcme.pvpplugin.json.jsonData.JSONMap;
import com.mcmiddleearth.mcme.pvpplugin.json.jsonData.jsonGamemodes.*;

public class MapEditor {

    enum EditorState{
        MAP,
        CAPTURETHEFLAG,
        FREEFORALL,
        INFECTED,
        TEAMCONQUEST,
        TEAMDEATHMATCH,
        TEAMSLAYER
    }
    EditorState state;

    JSONMap map;

    public MapEditor(JSONMap map){
        this.map = map;
        state = EditorState.MAP;
    }

    public void setMax(Integer max){
        if(state == EditorState.MAP){

        }else{
            state.getGamemode(map);
        }
    }

    public void setState(EditorState state){
        this.state = state;
    }
}

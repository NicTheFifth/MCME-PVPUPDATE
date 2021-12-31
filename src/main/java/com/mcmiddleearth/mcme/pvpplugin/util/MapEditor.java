package com.mcmiddleearth.mcme.pvpplugin.util;

import com.mcmiddleearth.mcme.pvpplugin.json.jsonData.JSONMap;
import com.mcmiddleearth.mcme.pvpplugin.json.jsonData.jsonGamemodes.*;

public class MapEditor {

    enum EditorState{
        MAP,
        CAPTURETHEFLAG{
            public JSONCaptureTheFlag getGamemode(JSONMap map){
                return map.getJSONCaptureTheFlag();
            }
        },
        FREEFORALL{
            public JSONFreeForAll getGamemode(JSONMap map){
                return map.getJSONFreeForAll();
            }
        },
        INFECTED{
            public JSONInfected getGamemode(JSONMap map){
                return map.getJSONInfected();
            }
        },
        TEAMCONQUEST{
            public JSONTeamConquest getGamemode(JSONMap map){
                return map.getJSONTeamConquest();
            }
        },
        TEAMDEATHMATCH{
            public JSONTeamDeathMatch getGamemode(JSONMap map){
                return map.getJSONTeamDeathMatch();
            }
        },
        TEAMSLAYER{
            public JSONTeamSlayer getGamemode(JSONMap map){
                return map.getJSONTeamSlayer();
            }
        };

        public void getGamemode(JSONMap map) {
        }
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

package com.mcmiddleearth.pvpplugin.runners.gamemodes;

import com.mcmiddleearth.pvpplugin.json.jsonData.JSONMap;
import com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes.JSONTeamConquest;
import com.mcmiddleearth.pvpplugin.json.transcribers.AreaTranscriber;
import com.mcmiddleearth.pvpplugin.runners.gamemodes.abstractions.GamemodeRunner;
import com.mcmiddleearth.pvpplugin.util.Team;

public class TeamConquest extends GamemodeRunner {

    TCTeam blue = new TCTeam();
    TCTeam red = new TCTeam();

    int scoreGoal;
    public static int DefaultScoreGoal(){
        return 20;
    }

    public TeamConquest(JSONMap map, int scoreGoal){
        region = AreaTranscriber.TranscribeArea(map);
        this.scoreGoal = scoreGoal;
        JSONTeamConquest teamConquest = map.getJSONTeamConquest();
        maxPlayers = teamConquest.getMaximumPlayers();
        mapName = map.getTitle();
        eventListener = new TCListener();
        initTeams(map);
        initStartConditions();
        initStartActions();
        initEndActions();
        initJoinConditions();
        initJoinActions();
        initLeaveActions();
    }
    private void initTeams(JSONMap map){

    }
    @Override
    protected void initStartConditions() {

    }

    @Override
    protected void initStartActions() {

    }

    @Override
    protected void initEndActions() {

    }

    @Override
    protected void initJoinConditions() {

    }

    @Override
    protected void initJoinActions() {

    }

    @Override
    protected void initLeaveActions() {

    }

    @Override
    public String getGamemode() {
        return "TeamConquest";
    }
    public class TCListener extends GamemodeListener{

        @Override
        protected void initOnPlayerDeathActions() {

        }
    }
    public static class TCTeam extends Team{
        private int points = 0;

        public void addPoints(int pointsToAdd) {
            points += pointsToAdd;
        }
    }
}

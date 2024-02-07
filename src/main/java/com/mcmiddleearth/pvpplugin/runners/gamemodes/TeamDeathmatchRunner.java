package com.mcmiddleearth.pvpplugin.runners.gamemodes;

import com.mcmiddleearth.pvpplugin.runners.gamemodes.abstractions.GamemodeRunner;

public class TeamDeathmatchRunner extends GamemodeRunner {

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
        return null;
    }
    public class TDMListener extends GamemodeListener{
        @Override
        protected void initOnPlayerDeathActions() {

        }
    }
}
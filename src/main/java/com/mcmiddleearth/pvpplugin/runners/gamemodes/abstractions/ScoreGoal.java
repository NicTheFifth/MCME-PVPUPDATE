package com.mcmiddleearth.pvpplugin.runners.gamemodes.abstractions;

public interface ScoreGoal {
    int getScoreGoal();
    void setScoreGoal(int scoreGoal);
    static int DefaultScoreGoal(){return 20;}
}

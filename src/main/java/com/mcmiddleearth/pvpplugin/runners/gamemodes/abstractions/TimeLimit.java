package com.mcmiddleearth.pvpplugin.runners.gamemodes.abstractions;

public interface TimeLimit {
    int getTimeLimit();
    void setTimeLimit(int timeLimit);
    static int DefaultTimeLimit(){return 300;}
}

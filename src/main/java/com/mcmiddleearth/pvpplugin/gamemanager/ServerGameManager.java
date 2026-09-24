package com.mcmiddleearth.pvpplugin.gamemanager;

import com.mcmiddleearth.pvpplugin.runners.gamemodes.abstractions.GamemodeRunner;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Supplier;

public class ServerGameManager implements GameManager{
    private GamemodeRunner activeGame;
    private Queue<Supplier<GamemodeRunner>> gameQueue = new LinkedList<>();

    @Override
    public void AddGame(GamemodeRunner runner, Player player){
        if(activeGame == null) {
            activeGame = runner;
            return;
        }
        gameQueue.add(() -> runner);
    }
    public void RemoveGame(Player player){
        if(gameQueue.isEmpty()){
            activeGame = null;
            return;
        }
        activeGame = gameQueue.poll().get();
    }
}

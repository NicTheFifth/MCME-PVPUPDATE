package com.mcmiddleearth.pvpplugin.gamemanager;

import com.mcmiddleearth.pvpplugin.runners.gamemodes.abstractions.GamemodeRunner;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.function.Supplier;

public class MinigameGameManager implements GameManager{
    private final Map<UUID,GamemodeRunner> activeGames  = new HashMap<>();
    private final Map<UUID, Queue<Supplier<GamemodeRunner>>> gameQueues = new HashMap<>();

    @Override
    public void AddGame(GamemodeRunner runner, Player player) {
        UUID uuid = player.getUniqueId();
        if(activeGames.get(uuid) == null){
            activeGames.put(uuid, runner);
            return;
        }
        Queue<Supplier<GamemodeRunner>> queue = gameQueues.getOrDefault(uuid, new LinkedList<>());
        queue.add(() -> runner);
        gameQueues.put(uuid, queue);
    }

    @Override
    public void RemoveGame(Player player) {
        UUID uuid = player.getUniqueId();
        Queue<Supplier<GamemodeRunner>> gameQueue =gameQueues.get(uuid);
        if(gameQueue == null || gameQueue.isEmpty()){
            activeGames.put(uuid, null);
            return;
        }
        activeGames.put(uuid, gameQueue.poll().get());
    }
}

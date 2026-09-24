package com.mcmiddleearth.pvpplugin.gamemanager;

import com.mcmiddleearth.pvpplugin.runners.gamemodes.abstractions.GamemodeRunner;
import org.bukkit.entity.Player;

public interface GameManager {
    void AddGame(GamemodeRunner runner, Player player);
    void RemoveGame(Player player);
}

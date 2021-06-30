package com.mcmiddleearth.mcme.pvpplugin.runners;

import org.bukkit.entity.Player;


public interface GamemodeRunner {

    boolean CanStart();

    void Start();

    void Run();

    void End(boolean stopped);

    boolean CanJoin(Player player);

    void Join(Player player);

    void Leave(Player player);
}

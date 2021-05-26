package com.mcmiddleearth.mcme.pvpplugin.runners;

import org.bukkit.entity.Player;


public interface GamemodeRunner {

    boolean CanStart();

    void Start();

    void End();

    boolean CanJoin(Player player);

    void Join(Player player);

    void Leave(Player player);
}

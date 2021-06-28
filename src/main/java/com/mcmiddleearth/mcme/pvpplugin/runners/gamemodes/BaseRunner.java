package com.mcmiddleearth.mcme.pvpplugin.runners.gamemodes;

import com.mcmiddleearth.mcme.pvpplugin.PVPPlugin;
import com.mcmiddleearth.mcme.pvpplugin.runners.GamemodeRunner;
import com.mcmiddleearth.mcme.pvpplugin.util.Matchmaker;
import com.mcmiddleearth.mcme.pvpplugin.util.Style;
import com.mcmiddleearth.mcme.pvpplugin.util.Team;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

import java.util.Set;

public abstract class BaseRunner implements GamemodeRunner {

    PVPPlugin pvpPlugin;

    enum State
        {QUEUED, COUNTDOWN, RUNNING};

    @Getter@Setter
    State gameState;

    @Getter@Setter
    Set<Player> players;

    @Getter@Setter
    Team spectator;

    @Getter@Setter
    Integer maxPlayers;

    @Getter@Setter
    boolean privateGame;

    @Getter@Setter
    Set<Player> whitelistedPlayers;

    Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

    @Override
    public boolean CanStart() {
        return !players.isEmpty();
    }

    @Override
    public void Start() {
        gameState = State.RUNNING;
    }

    @Override
    public void End(boolean stopped) {

    }

    @Override
    public boolean CanJoin(Player player) {
        return maxPlayers > players.size() && !players.contains((player)) && gameState != State.COUNTDOWN;
    }

    @Override
    public void Join(Player player) {
        players.add(player);
    }

    @Override
    public void Leave(Player player){
        if(players.remove(player)){
            player.sendMessage(Style.INFO + "You have left the game.");
        }
    }
}
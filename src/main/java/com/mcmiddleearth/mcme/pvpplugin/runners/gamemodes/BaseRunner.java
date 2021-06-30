package com.mcmiddleearth.mcme.pvpplugin.runners.gamemodes;

import com.mcmiddleearth.mcme.pvpplugin.PVPPlugin;
import com.mcmiddleearth.mcme.pvpplugin.runners.GamemodeRunner;
import com.mcmiddleearth.mcme.pvpplugin.runners.runnerUtil.ScoreboardEditor;
import com.mcmiddleearth.mcme.pvpplugin.runners.runnerUtil.TeamHandler;
import com.mcmiddleearth.mcme.pvpplugin.util.Matchmaker;
import com.mcmiddleearth.mcme.pvpplugin.util.Style;
import com.mcmiddleearth.mcme.pvpplugin.util.Team;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;
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

    private long countDowntimer = 5;

    Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

    @Override
    public boolean CanStart() {
        return !players.isEmpty();
    }

    @Override
    public void Start() {
        gameState = State.COUNTDOWN;
        spectator.getMembers().forEach(player -> player.teleport(spectator.getSpawnLocations().get(0)));
        TeamHandler.setGamemode(GameMode.SPECTATOR, spectator);
        new BukkitRunnable() {
            @Override
            public void run() {
                players.forEach(player -> player.sendMessage(ChatColor.GREEN + "Game starts in " + countDowntimer));
                countDowntimer--;
            }
        }.runTaskTimer(pvpPlugin, 0, 20L * countDowntimer + 20);
        gameState = State.RUNNING;
    }

    @Override
    public void End(boolean stopped) {
        players.forEach(player-> {
            player.getInventory().clear();
            player.getActivePotionEffects().clear();
            player.setGameMode(GameMode.ADVENTURE);
        });
        spectator.getMembers().forEach(player -> pvpPlugin.getPlayerstats().get(player.getUniqueId()).addSpectate());
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

    public void HandleKill(PlayerDeathEvent playerDeath){

    }
}
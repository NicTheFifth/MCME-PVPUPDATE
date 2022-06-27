package com.mcmiddleearth.mcme.pvpplugin.runners.gamemodes;

import com.mcmiddleearth.mcme.pvpplugin.PVPPlugin;
import com.mcmiddleearth.mcme.pvpplugin.runners.GamemodeRunner;
import com.mcmiddleearth.mcme.pvpplugin.runners.runnerUtil.TeamHandler;
import com.mcmiddleearth.command.Style;
import com.mcmiddleearth.mcme.pvpplugin.util.Team;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

public abstract class BaseRunner implements GamemodeRunner {

    enum State {
        QUEUED, COUNTDOWN, RUNNING
    }

    State gameState;
    Set<Player> players;
    Team spectator;
    Integer maxPlayers;

    Region region;
    HashMap<UUID, Long> lastOutOfBounds = new HashMap<>();

    private long countDownTimer = 5;

    Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

    boolean privateGame;
    Set<Player> whitelistedPlayers;

    @Override
    public boolean canStart() {
        return !players.isEmpty();
    }

    @Override
    public void start() {
        gameState = State.COUNTDOWN;
        spectator.getMembers().forEach(player -> player.teleport(spectator.getSpawnLocations().get(0)));
        TeamHandler.setGamemode(GameMode.SPECTATOR, spectator);
        new BukkitRunnable() {
            @Override
            public void run() {
                players.forEach(player -> player.sendMessage(ChatColor.GREEN + "Game starts in " + countDownTimer));
                countDownTimer--;
            }
        }.runTaskTimer(PVPPlugin.getInstance(), 0, 20L * countDownTimer + 20);
        gameState = State.RUNNING;
    }

    @Override
    public void end(boolean stopped) {
        players.forEach(player -> {
            player.getInventory().clear();
            player.getActivePotionEffects().clear();
            player.setGameMode(GameMode.ADVENTURE);
        });
        if (!stopped) spectator.getMembers().forEach(player -> PVPPlugin.getInstance().getPlayerstats().get(player.getUniqueId()).addSpectate());
    }

    protected boolean canJoin(Player player) {
        return maxPlayers > players.size() &&
                !players.contains((player)) &&
                gameState != State.COUNTDOWN &&
                (!privateGame || whitelistedPlayers.contains(player));
    }

    @Override
    public String[] join(Player player) {
        players.add(player);
        return null;
    }

    @Override
    public void leave(Player player) {
        if (players.remove(player)) {
            player.sendMessage(Style.INFO + "You have left the game.");
        }
    }

    //<editor-fold desc="EventHandlers">
    public void handleDeath(PlayerDeathEvent playerDeath) {
        Player player = playerDeath.getEntity();
        PVPPlugin.getInstance().getPlayerstats().get(player.getUniqueId()).addDeath();
        if (player.getKiller() != null) {
            PVPPlugin.getInstance().getPlayerstats().get(player.getKiller().getUniqueId()).addKill();
        }
    }

    @EventHandler
    public void playerMove(PlayerMoveEvent playerMove) {
        if (players.contains(playerMove.getPlayer())) {
            if (gameState == State.COUNTDOWN) {
                playerMove.setCancelled(true);
                return;
            }
            stayInBorder(playerMove);
        }
    }

    private void stayInBorder(PlayerMoveEvent playerMove) {
        Location newLoc = playerMove.getTo();
        if (!region.contains(BlockVector3.at(newLoc.getX(), newLoc.getY(), newLoc.getZ()))) {
            playerMove.setCancelled(true);
            sendOutOfBoundsWarning(playerMove.getPlayer());
        }
    }

    private void sendOutOfBoundsWarning(Player player) {
        if (!lastOutOfBounds.containsKey(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "You aren't allowed to leave the map!");
            lastOutOfBounds.put(player.getUniqueId(), System.currentTimeMillis());
        } else if (System.currentTimeMillis() - lastOutOfBounds.get(player.getUniqueId()) > 3000) {
            player.sendMessage(ChatColor.RED + "You aren't allowed to leave the map!");
            lastOutOfBounds.put(player.getUniqueId(), System.currentTimeMillis());
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public State getGameState() {
        return this.gameState;
    }
    public void setGameState(final State gameState) {
        this.gameState = gameState;
    }

    public Set<Player> getPlayers() {
        return this.players;
    }
    public void setPlayers(final Set<Player> players) {
        this.players = players;
    }

    public Team getSpectator() {
        return this.spectator;
    }
    public void setSpectator(final Team spectator) {
        this.spectator = spectator;
    }

    public Integer getMaxPlayers() {
        return this.maxPlayers;
    }
    public void setMaxPlayers(final Integer maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public Region getRegion() {
        return this.region;
    }
    public void setRegion(final Region region) {
        this.region = region;
    }

    public boolean isPrivateGame() {
        return this.privateGame;
    }
    public void setPrivateGame(final boolean privateGame) {
        this.privateGame = privateGame;
    }

    public Set<Player> getWhitelistedPlayers() {
        return this.whitelistedPlayers;
    }
    public void setWhitelistedPlayers(final Set<Player> whitelistedPlayers) {
        this.whitelistedPlayers = whitelistedPlayers;
    }
    //</editor-fold>
}

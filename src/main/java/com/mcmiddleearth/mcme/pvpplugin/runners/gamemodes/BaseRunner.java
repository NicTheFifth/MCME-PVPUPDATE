package com.mcmiddleearth.mcme.pvpplugin.runners.gamemodes;

import com.mcmiddleearth.mcme.pvpplugin.PVPPlugin;
import com.mcmiddleearth.mcme.pvpplugin.runners.GamemodeRunner;
import com.mcmiddleearth.mcme.pvpplugin.runners.runnerUtil.TeamHandler;
import com.mcmiddleearth.mcme.pvpplugin.util.Style;
import com.mcmiddleearth.mcme.pvpplugin.util.Team;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

public abstract class BaseRunner implements GamemodeRunner {

    enum State {
        QUEUED, COUNTDOWN, RUNNING
    }

    PVPPlugin pvpPlugin;
    State gameState;
    Set<Player> players;
    Team spectator;
    Integer maxPlayers;
    Region region;
    HashMap<UUID, Long> lastOutOfBounds = new HashMap<>();
    private long countDownTimer = 5;
    Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
    boolean privateGame;
    Set<Player> whitelistedPlayers;

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
                players.forEach(player -> player.sendMessage(ChatColor.GREEN + "Game starts in " + countDownTimer));
                countDownTimer--;
            }
        }.runTaskTimer(pvpPlugin, 0, 20L * countDownTimer + 20);
        gameState = State.RUNNING;
    }

    @Override
    public void End(boolean stopped) {
        players.forEach(player -> {
            player.getInventory().clear();
            player.getActivePotionEffects().clear();
            player.setGameMode(GameMode.ADVENTURE);
        });
        if (!stopped) spectator.getMembers().forEach(player -> pvpPlugin.getPlayerstats().get(player.getUniqueId()).addSpectate());
    }

    @Override
    public boolean CanJoin(Player player) {
        return maxPlayers > players.size() && !players.contains((player)) && gameState != State.COUNTDOWN && (!privateGame || whitelistedPlayers.contains(player));
    }

    @Override
    public void Join(Player player) {
        players.add(player);
    }

    @Override
    public void Leave(Player player) {
        if (players.remove(player)) {
            player.sendMessage(Style.INFO + "You have left the game.");
        }
    }

    public void HandleDeath(PlayerDeathEvent playerDeath) {
        Player player = playerDeath.getEntity();
        pvpPlugin.getPlayerstats().get(player.getUniqueId()).addDeath();
        if (player.getKiller() != null) {
            pvpPlugin.getPlayerstats().get(player.getKiller().getUniqueId()).addKill();
        }
    }

    @EventHandler
    public void PlayerMove(PlayerMoveEvent playerMove) {
        if (players.contains(playerMove.getPlayer())) {
            if (gameState == State.COUNTDOWN) {
                playerMove.setCancelled(true);
                return;
            }
            StayInBorder(playerMove);
        }
    }

    private void StayInBorder(PlayerMoveEvent playerMove) {
        Location newLoc = playerMove.getTo();
        if (!region.contains(BlockVector3.at(newLoc.getX(), newLoc.getY(), newLoc.getZ()))) {
            playerMove.setCancelled(true);
            SendOutOfBoundsWarning(playerMove.getPlayer());
        }
    }

    private void SendOutOfBoundsWarning(Player player) {
        if (!lastOutOfBounds.containsKey(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "You aren't allowed to leave the map!");
            lastOutOfBounds.put(player.getUniqueId(), System.currentTimeMillis());
        } else if (System.currentTimeMillis() - lastOutOfBounds.get(player.getUniqueId()) > 3000) {
            player.sendMessage(ChatColor.RED + "You aren't allowed to leave the map!");
            lastOutOfBounds.put(player.getUniqueId(), System.currentTimeMillis());
        }
    }

    //<editor-fold defaultstate="collapsed" desc="delombok">
    @SuppressWarnings("all")
    public State getGameState() {
        return this.gameState;
    }

    @SuppressWarnings("all")
    public void setGameState(final State gameState) {
        this.gameState = gameState;
    }

    @SuppressWarnings("all")
    public Set<Player> getPlayers() {
        return this.players;
    }

    @SuppressWarnings("all")
    public void setPlayers(final Set<Player> players) {
        this.players = players;
    }

    @SuppressWarnings("all")
    public Team getSpectator() {
        return this.spectator;
    }

    @SuppressWarnings("all")
    public void setSpectator(final Team spectator) {
        this.spectator = spectator;
    }

    @SuppressWarnings("all")
    public Integer getMaxPlayers() {
        return this.maxPlayers;
    }

    @SuppressWarnings("all")
    public void setMaxPlayers(final Integer maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    @SuppressWarnings("all")
    public Region getRegion() {
        return this.region;
    }

    @SuppressWarnings("all")
    public void setRegion(final Region region) {
        this.region = region;
    }

    @SuppressWarnings("all")
    public boolean isPrivateGame() {
        return this.privateGame;
    }

    @SuppressWarnings("all")
    public void setPrivateGame(final boolean privateGame) {
        this.privateGame = privateGame;
    }

    @SuppressWarnings("all")
    public Set<Player> getWhitelistedPlayers() {
        return this.whitelistedPlayers;
    }

    @SuppressWarnings("all")
    public void setWhitelistedPlayers(final Set<Player> whitelistedPlayers) {
        this.whitelistedPlayers = whitelistedPlayers;
    }
    //</editor-fold>
}

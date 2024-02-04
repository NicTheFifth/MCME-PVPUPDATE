package com.mcmiddleearth.pvpplugin.runners.gamemodes.abstractions;

import com.mcmiddleearth.command.Style;
import com.mcmiddleearth.pvpplugin.PVPPlugin;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONLocation;
import com.mcmiddleearth.pvpplugin.json.transcribers.LocationTranscriber;
import com.mcmiddleearth.pvpplugin.runners.runnerUtil.TeamHandler;
import com.mcmiddleearth.pvpplugin.util.Matchmaker;
import com.mcmiddleearth.pvpplugin.util.PlayerStatEditor;
import com.mcmiddleearth.pvpplugin.util.Team;
import com.sk89q.worldedit.regions.Region;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.mcmiddleearth.pvpplugin.command.CommandUtil.sendBaseComponent;


public abstract class GamemodeRunner implements Listener {
    protected enum State{
        QUEUED, COUNTDOWN, RUNNING, ENDED
    }
    protected int maxPlayers;
    protected State gameState;
    protected long countDownTimer;//TODO: default countdown timer
    protected boolean isPrivate;
    protected Set<Player> whiteList = new HashSet<>();
    protected Set<Player> players = new HashSet<>();
    protected Team spectator = new Team();
    protected Region region;

    public GamemodeRunner(){
        gameState = State.QUEUED;
        startConditions.put(()-> players.size() < 2,
            new ComponentBuilder("Can't start the game with less than two " +
                "players.").color(Style.ERROR).create());

        joinConditions.put(player -> players.size() >= maxPlayers,
            new ComponentBuilder("Can't join the game as it is full.")
                .color(Style.ERROR).create());
        joinConditions.put(player -> gameState == State.COUNTDOWN,
            new ComponentBuilder("Can't join the game during countdown, try " +
                "again after the countdown is finished.")
                .color(Style.ERROR).create());

        joinActions.add(players::add);
        joinActions.add(player -> spectator.getMembers().remove(player));

        leaveActions.add(players::remove);
    }
    protected void initSpectator(JSONLocation spawn){
        spectator.setPrefix("Spectator");
        spectator.setTeamColour(Color.SILVER);
        spectator.setSpawnLocations(new ArrayList<>(List.of(
            LocationTranscriber.TranscribeFromJSON(spawn)
        )));
        spectator.setGameMode(GameMode.SPECTATOR);
    }
    //<editor-fold defaultstate="collapsed" desc="Start conditions">
    protected Map<Supplier<Boolean>, BaseComponent[]> startConditions =
        new HashMap<>();
    public boolean canStart(Player player){
        List<BaseComponent[]> errorMessage =
            startConditions.entrySet().stream()
                .filter(condition -> !condition.getKey().get())
                .map(Map.Entry::getValue).collect(Collectors.toList());
        if(errorMessage.isEmpty()) {
            return true;
        }
        errorMessage.forEach(message -> sendBaseComponent(message, player));
        return false;
    }
    protected abstract void initStartConditions();
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Start game">
    protected List<Runnable> startActions = List.of();
    public void start(){
        startActions.forEach(Runnable::run);
        CountDown();
    }
    protected abstract void initStartActions();
    private void CountDown() {
        gameState = State.COUNTDOWN;
        new BukkitRunnable() {
            @Override
            public void run() {
                if (countDownTimer == 0) {
                    players.forEach(player -> player.sendMessage(ChatColor.GREEN + "Game starts!"));
                    gameState = State.RUNNING;
                    this.cancel();
                    return;
                }
                players.forEach(player -> player.sendMessage(ChatColor.GREEN + "Game starts in " + countDownTimer));
                countDownTimer--;
            }
        }.runTaskTimer(PVPPlugin.getInstance(),0,20);
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="End game">
    //EndActions is a map where the boolean indicates stopped state
    protected Map<Boolean, List<Runnable>> endActions = Map.of(true,
        List.of(), false, List.of());
    public void end(boolean stopped){
        PVPPlugin pvpPlugin = PVPPlugin.getInstance();
        players.forEach(player -> {
            player.getInventory().clear();
            player.getActivePotionEffects().clear();
            player.setGameMode(GameMode.ADVENTURE);
            player.teleport(pvpPlugin.getSpawn());
        });
        gameState = State.ENDED;
        pvpPlugin.setActiveGame(null);
        if(stopped){
            endActions.get(true).forEach(Runnable::run);
            return;
        }
        endActions.get(false).forEach(Runnable::run);
        spectator.getMembers().forEach(PlayerStatEditor::addSpectate);
    }
    protected abstract void initEndActions();
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Joining">
    protected Map<Predicate<Player>, BaseComponent[]> joinConditions =
        new HashMap<>();
    public boolean canJoin(Player player){
        List<BaseComponent[]> rejectedMessages =
            joinConditions.entrySet().stream()
            .filter(entry -> !entry.getKey().test(player))
            .map(Map.Entry::getValue).collect(Collectors.toList());
        if(rejectedMessages.isEmpty())
            return true;
        rejectedMessages.forEach(message -> sendBaseComponent(message,
            player));
        return false;
    }
    protected abstract void initJoinConditions();
    protected List<Consumer<Player>> joinActions = List.of();
    public void Join(Player player){
        joinActions.forEach(joinAction -> joinAction.accept(player));
    }
    protected abstract void initJoinActions();
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Leave">
    protected List<Consumer<Player>> leaveActions = List.of();
    public void leaveGame(Player player, boolean hasLeftGame){
        leaveActions.forEach(leaveAction -> {
            if(gameState!=State.ENDED)
                leaveAction.accept(player);});
        if(gameState == State.ENDED)
            return;
        if(hasLeftGame)
            return;
        Matchmaker.addMember(player, spectator);
        TeamHandler.spawn(player, spectator);
        sendBaseComponent(new ComponentBuilder("You are now spectating.")
            .color(Style.INFO).create(),
            player);
    }
    protected abstract  void  initLeaveActions();
    //</editor-fold>
}

package com.mcmiddleearth.pvpplugin.runners.gamemodes.abstractions;

import com.mcmiddleearth.command.Style;
import com.mcmiddleearth.pvpplugin.PVPPlugin;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONLocation;
import com.mcmiddleearth.pvpplugin.json.transcribers.LocationTranscriber;
import com.mcmiddleearth.pvpplugin.mapeditor.MapEditor;
import com.mcmiddleearth.pvpplugin.runners.runnerUtil.TeamHandler;
import com.mcmiddleearth.pvpplugin.util.Matchmaker;
import com.mcmiddleearth.pvpplugin.util.PlayerStatEditor;
import com.mcmiddleearth.pvpplugin.util.Team;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

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
    protected String mapName;
    protected int maxPlayers;
    protected State gameState;
    protected long countDownTimer = 5;//TODO: default countdown timer
    //protected boolean isPrivate;
    //protected Set<Player> whiteList = new HashSet<>();
    protected Set<Player> players = new HashSet<>();
    protected Team spectator = new Team();
    protected Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
    protected Region region;
    protected Listener eventListener;

    public GamemodeRunner(){
        gameState = State.QUEUED;
        startConditions.put(()-> players.size() >= 2,
            new ComponentBuilder("Can't start the game with less than two " +
                "players.").color(Style.ERROR).create());

        startActions.add(() ->
            players.forEach(player -> player.setScoreboard(scoreboard)));
        startActions.add(() ->
            spectator.getMembers().forEach(player -> player.setScoreboard(scoreboard)));
        startActions.add(() -> MapEditor.hideSpawns(null, false));
        startActions.add(() ->TeamHandler.spawnAll(spectator));

        joinConditions.put(player -> players.size() < maxPlayers,
            new ComponentBuilder("Can't join the game as it is full.")
                .color(Style.ERROR).create());
        joinConditions.put(player -> gameState != State.COUNTDOWN,
            new ComponentBuilder("Can't join the game during countdown, try " +
                "again after the countdown is finished.")
                .color(Style.ERROR).create());
        joinConditions.put(player -> !players.contains(player),
            new ComponentBuilder("Can't join the game, you've already joined it.")
                    .color(Style.ERROR).create()
        );

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
    protected List<Runnable> startActions = new ArrayList<>();
    public void start(){
        PVPPlugin.addEventListener(eventListener);
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
        new ArrayList<>(), false, new ArrayList<>());
    public void end(boolean stopped){
        PVPPlugin pvpPlugin = PVPPlugin.getInstance();
        PlayerDeathEvent.getHandlerList().unregister(eventListener);
        PlayerQuitEvent.getHandlerList().unregister(eventListener);
        PlayerMoveEvent.getHandlerList().unregister(eventListener);
        players.forEach(player -> {
            player.getInventory().clear();
            player.getActivePotionEffects().clear();
            player.setGameMode(GameMode.SURVIVAL);
            //player.teleport(pvpPlugin.getSpawn());
        });
        scoreboard.getObjectives().forEach(Objective::unregister);
        gameState = State.ENDED;
        endActions.get(false).forEach(Runnable::run);
        if(!stopped)
            spectator.getMembers().forEach(PlayerStatEditor::addSpectate);
        Consumer<Player> message = player ->
                sendBaseComponent(
                        new ComponentBuilder(String.format("%s on %s has ended.", getGamemode(), mapName)).create(), player);
        spectator.getMembers().forEach(message);
        players.forEach(message);

        Supplier<GamemodeRunner> nextGame = pvpPlugin.getGameQueue().poll();
        if(nextGame != null)
            pvpPlugin.setActiveGame(nextGame.get());
        else
            pvpPlugin.setActiveGame(null);
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

    protected List<Consumer<Player>> joinActions = new ArrayList<>();

    public void Join(Player player){
        joinActions.forEach(joinAction -> joinAction.accept(player));
    }

    protected abstract void initJoinActions();
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Leave">
    protected List<Consumer<Player>> leaveActions = new ArrayList<>();
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
    public String getMapName() {
        return mapName;
    }

    public abstract String getGamemode();

    protected abstract class GamemodeListener implements Listener {
        HashMap<UUID, Long> playerAreaLeaveTimer = new HashMap<>();
        protected List<Consumer<PlayerDeathEvent>> onPlayerDeathActions =
            new ArrayList<>();

        @EventHandler
        public void onPlayerDeath(PlayerDeathEvent e){
            Player player = e.getEntity();
            if(!players.contains(player))
                return;
            PlayerStatEditor.addDeath(player);
            Player killer = player.getKiller();
            if(killer != null)
                PlayerStatEditor.addKill(killer);
            onPlayerDeathActions.forEach(action -> action.accept(e));
        }

        protected abstract void initOnPlayerDeathActions();

        @EventHandler
        public void onPlayerLeave(PlayerQuitEvent e){
            Player player = e.getPlayer();
            spectator.getMembers().remove(player);
            player.getInventory().clear();
            player.setGameMode(GameMode.SURVIVAL);
            player.teleport(PVPPlugin.getInstance().getSpawn());
            leaveGame(player, true);
        }

        @EventHandler
        public void onPlayerMove(PlayerMoveEvent e){
            Location from = e.getFrom();
            Location to = e.getTo();

            Player player = e.getPlayer();
            UUID uuid = player.getUniqueId();

            if(gameState == State.QUEUED || to == null)
                return;
            if(gameState == State.COUNTDOWN &&
                    players.contains(e.getPlayer()) &&
                    (from.getX() != to.getX() || from.getZ() != to.getZ())) {
                e.setCancelled(true);
                return;
            }

            if(!region.contains(BlockVector3.at(to.getX(), to.getY(), to.getZ()))){
                e.setCancelled(true);
                if(!playerAreaLeaveTimer.containsKey(uuid)){
                    player.sendMessage(ChatColor.RED + "You aren't allowed to leave the map!");
                    playerAreaLeaveTimer.put(uuid, System.currentTimeMillis());
                    return;
                }
                if(System.currentTimeMillis() - playerAreaLeaveTimer.get(uuid) > 3000){
                    player.sendMessage(ChatColor.RED + "You aren't allowed to leave the map!");
                    playerAreaLeaveTimer.put(uuid, System.currentTimeMillis());
                }
            }
        }

        @EventHandler
        public void onInventoryClick(InventoryClickEvent e){
            Player player = (Player) e.getWhoClicked();
            if(!players.contains(player))
                return;
            if(e.getSlotType() == InventoryType.SlotType.ARMOR)
                e.setCancelled(true);
        }

        @EventHandler
        public void returnDroppedItems(PlayerDropItemEvent e){
            Player player = e.getPlayer();
            if(!players.contains(player))
                return;
            e.setCancelled(true);
        }
    }
}

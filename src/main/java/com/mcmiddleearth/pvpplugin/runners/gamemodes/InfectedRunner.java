package com.mcmiddleearth.pvpplugin.runners.gamemodes;

import com.mcmiddleearth.command.Style;
import com.mcmiddleearth.pvpplugin.PVPPlugin;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONLocation;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONMap;
import com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes.JSONInfected;
import com.mcmiddleearth.pvpplugin.json.transcribers.AreaTranscriber;
import com.mcmiddleearth.pvpplugin.json.transcribers.LocationTranscriber;
import com.mcmiddleearth.pvpplugin.runners.gamemodes.abstractions.GamemodeRunner;
import com.mcmiddleearth.pvpplugin.runners.gamemodes.abstractions.TimeLimit;
import com.mcmiddleearth.pvpplugin.runners.runnerUtil.ChatUtils;
import com.mcmiddleearth.pvpplugin.runners.runnerUtil.KitEditor;
import com.mcmiddleearth.pvpplugin.runners.runnerUtil.ScoreboardEditor;
import com.mcmiddleearth.pvpplugin.runners.runnerUtil.TeamHandler;
import com.mcmiddleearth.pvpplugin.statics.Gamemodes;
import com.mcmiddleearth.pvpplugin.util.Kit;
import com.mcmiddleearth.pvpplugin.util.Matchmaker;
import com.mcmiddleearth.pvpplugin.util.PlayerStatEditor;
import com.mcmiddleearth.pvpplugin.util.Team;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.mcmiddleearth.pvpplugin.command.CommandUtil.sendBaseComponent;

public class InfectedRunner extends GamemodeRunner implements TimeLimit {
    int timeLimit;

    public static int DefaultTimeLimit(){
        return 300;
    }

    Team survivors = new Team();
    Team infected = new Team();

    public InfectedRunner(JSONMap map, int timeLimit){
        region = AreaTranscriber.TranscribeArea(map);
        JSONInfected infected = map.getJSONInfected();
        this.timeLimit = timeLimit;
        maxPlayers = infected.getMaximumPlayers();
        mapName = map.getTitle();
        eventListener = new IListener();
        initTeams(map);
        initStartConditions();
        initStartActions();
        initEndActions();
        initJoinConditions();
        initJoinActions();
        initLeaveActions();
        ChatUtils.AnnounceNewGame("Infected", mapName, String.valueOf(maxPlayers));
    }
    //<editor-fold defaultstate="collapsed" desc="Teams">
    private void initTeams(JSONMap map){
        initSpectator(map.getSpawn());
        initSurvivor(map.getJSONInfected().getSurvivorSpawn());
        initInfected(map.getJSONInfected().getInfectedSpawn());
    }
    private void initSurvivor(JSONLocation survivorSpawn){
        survivors.setPrefix("Survivor");
        survivors.setTeamColour(Color.BLUE);
        survivors.setChatColor(ChatColor.BLUE);
        survivors.setGameMode(GameMode.SURVIVAL);
        survivors.setKit(createSurvivorKit());
        survivors.setSpawnLocations(List.of(LocationTranscriber.TranscribeFromJSON(survivorSpawn)));

    }
    private Kit createSurvivorKit(){
        Consumer<Player> invFunc = (player -> {
            createInfectedKit(Color.BLUE).accept(player);
            PlayerInventory returnInventory = player.getInventory();
            returnInventory.setHelmet(new ItemStack(Material.LEATHER_HELMET));
            returnInventory.setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
            returnInventory.setBoots(new ItemStack(Material.LEATHER_BOOTS));
            returnInventory.setItemInOffHand(new ItemStack(Material.SHIELD));
            returnInventory.forEach(item -> KitEditor.setItemColour(item,
                    Color.BLUE));
        });
        return new Kit(invFunc);

    }
    private void initInfected(JSONLocation infectedSpawn){
        infected.setPrefix("Infected");
        infected.setTeamColour(Color.RED);
        infected.setChatColor(ChatColor.RED);
        infected.setGameMode(GameMode.SURVIVAL);
        //TODO: Fix the infected kit to be slightly stronger
        infected.setKit(new Kit(createInfectedKit(Color.RED)));
        infected.setSpawnLocations(List.of(LocationTranscriber.TranscribeFromJSON(infectedSpawn)));
    }
    private @NotNull Consumer<Player> createInfectedKit(Color color){
        return (x -> {
            PlayerInventory returnInventory = x.getInventory();
            returnInventory.clear();
            returnInventory.setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
            returnInventory.setItem(0, new ItemStack(Material.IRON_SWORD));
            ItemStack bow = new ItemStack(Material.BOW);
            bow.addEnchantment(Enchantment.ARROW_INFINITE, 1);
            returnInventory.setItem(1, bow);
            returnInventory.setItem(2, new ItemStack(Material.ARROW));
            returnInventory.forEach(item -> KitEditor.setItemColour(item,
                    color));
        });
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Start Conditions">
    @Override
    protected void initStartConditions() {
        Supplier<Integer> totalInTeams = () ->
                infected.getOnlineMembers().size() + survivors.getOnlineMembers().size();
        startConditions.put(() -> totalInTeams.get() != players.size() || !infected.getOnlineMembers().isEmpty(),
                new ComponentBuilder("Can't start, infected has to have at least " +
                        "one online player.")
                        .color(Style.ERROR).create());
        startConditions.put(() -> totalInTeams.get() != players.size() ||!survivors.getOnlineMembers().isEmpty(),
                new ComponentBuilder("Can't start, survivors has to have at least" +
                        " one online player.")
                        .color(Style.ERROR).create());

    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Start Actions">
    @Override
    protected void initStartActions() {
        startActions.add(() -> {
            initWithRandomInfected();
            players.forEach(player -> JoinInfected(player, true));
        });
        startActions.add(()-> ScoreboardEditor.InitInfected(scoreboard, timeLimit, infected, survivors));
        startActions.add(() -> new BukkitRunnable() {
            @Override
            public void run() {
                if(gameState == State.COUNTDOWN)
                {
                    return;
                }
                if (timeLimit == 0) {
                    end(false);
                    gameState = State.ENDED;
                    this.cancel();
                    return;
                }
                timeLimit--;
                ScoreboardEditor.UpdateTimeInfected(scoreboard, timeLimit);
            }
        }.runTaskTimer(PVPPlugin.getInstance(),100,20));
    }

    private void initWithRandomInfected() {
        if (!infected.getOnlineMembers().isEmpty())
            return;
        Player player = null;
        while (survivors.getOnlineMembers().contains(player)) {
            int randomInfectedIndex = ThreadLocalRandom.current().nextInt(0, players.size() + 1);
            player = (Player) players.toArray()[randomInfectedIndex];
        }
        infected.getMembers().add(player);
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="End Actions">
    @Override
    protected void initEndActions() {
            endActions.get(false).add(() ->
                    getWinningTeamMembers().forEach(player -> {
                        PlayerStatEditor.addWon(player);
                        PlayerStatEditor.addPlayed(player);
                    }));
            endActions.get(false).add(() ->
                    getLosingTeamMembers().forEach(player -> {
                        PlayerStatEditor.addLost(player);
                        PlayerStatEditor.addPlayed(player);
                    }));
            endActions.get(false).add(() ->{
                if(survivors.getOnlineMembers().isEmpty())
                    players.forEach(player ->
                            sendBaseComponent(
                                    new ComponentBuilder("Infected Won!!!").color(ChatColor.RED)
                                            .create(), player)) ;
                else
                    players.forEach(player ->
                            sendBaseComponent(
                                    new ComponentBuilder("Survivors Won!!!").color(ChatColor.BLUE)
                                            .create(), player));});
            endActions.get(false).add(() -> PlayerRespawnEvent.getHandlerList().unregister(eventListener));
            endActions.get(true).add(()-> PlayerRespawnEvent.getHandlerList().unregister(eventListener));
    }
    private Set<Player> getLosingTeamMembers() {
        if(survivors.getOnlineMembers().isEmpty())
            return survivors.getMembers();
        return infected.getMembers();
    }
    private Set<Player> getWinningTeamMembers() {
        if(survivors.getOnlineMembers().isEmpty())
            return infected.getMembers();
        return survivors.getMembers();
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Join">
    @Override
    protected void initJoinConditions() {
        joinConditions.put((player ->
                        timeLimit <=60),
                new ComponentBuilder("The game is close to over, you cannot join.")
                        .color(Style.INFO)
                        .create());
    }

    @Override
    protected void initJoinActions() {
        joinActions.add(player -> JoinInfected(player, false));
    }

    private void JoinInfected(Player player, boolean onStart){
        if(!onStart && gameState == State.QUEUED) {
            sendBaseComponent(
                    new ComponentBuilder("You joined the game.").color(Style.INFO).create(),
                    player);
            return;
        }
        if(infected.getMembers().contains(player)) {
            JoinInfectedTeam(player);
            return;
        }
        if(survivors.getMembers().contains(player)) {
            JoinSurvivorsTeam(player);
            return;
        }
        TeamHandler.addToTeamInfected(
                Pair.of(infected, () -> JoinInfectedTeam(player)),
                Pair.of(survivors, () -> JoinSurvivorsTeam(player)));
    }

    private void JoinInfectedTeam(Player player){
        infected.getOnlineMembers().add(player);
        Matchmaker.addMember(player, infected);
        TeamHandler.spawn(player, infected);
        BaseComponent[] publicJoinMessage = new ComponentBuilder(
                String.format("%s has joined the infected!", player.getName()))
                .color(infected.getChatColor()).create();
        players.forEach(playerOther ->
                sendBaseComponent(publicJoinMessage, playerOther));
        spectator.getMembers().forEach(spectator ->
                sendBaseComponent(publicJoinMessage, spectator));
    }
    
    private void JoinSurvivorsTeam(Player player){
        survivors.getOnlineMembers().add(player);
        Matchmaker.addMember(player, survivors);
        TeamHandler.spawn(player, survivors);
        BaseComponent[] publicJoinMessage = new ComponentBuilder(
                String.format("%s has joined the survivors!", player.getName()))
                .color(survivors.getChatColor()).create();
        players.forEach(playerOther ->
                sendBaseComponent(publicJoinMessage, playerOther));
        spectator.getMembers().forEach(spectator ->
                sendBaseComponent(publicJoinMessage, spectator));
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Leave">
    @Override
    protected void initLeaveActions() {
        leaveActions.add(this::leave);
    }

    private void leave(Player player){
        if (infected.getMembers().contains(player)) {
            leaveInfected(player);
        } else {
            leaveSurvivors(player);
        }
        if(infected.getOnlineMembers().isEmpty())
            end(true);
        if(survivors.getOnlineMembers().isEmpty())
            end(false);
    }

    private void leaveInfected(Player player){
        infected.getOnlineMembers().remove(player);
        players.forEach(playerOther->sendBaseComponent(
                new ComponentBuilder(String.format("%s has left the game.",
                        player.getName()))
                        .color(infected.getChatColor()).create(),
                playerOther
        ));

    }

    private void leaveSurvivors(Player player){
        survivors.getOnlineMembers().remove(player);
        players.forEach(playerOther->sendBaseComponent(
                new ComponentBuilder(String.format("%s has left the game.",
                        player.getName()))
                        .color(survivors.getChatColor()).create(),
                playerOther
        ));
    }
    //</editor-fold>

    @Override
    public int getTimeLimit() {
        return timeLimit;
    }

    @Override
    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    @Override
    public String getGamemode() {
        return Gamemodes.INFECTED;
    }
    public class IListener extends GamemodeListener{
        public IListener(){
            initOnPlayerDeathActions();
        }
        @Override
        protected void initOnPlayerDeathActions() {
            onPlayerDeathActions.add(e -> {
                Player player = e.getEntity();
                if(survivors.getOnlineMembers().remove(player)) {
                    survivors.getMembers().remove(player);
                    Matchmaker.addMember(player, infected);
                    if(survivors.getOnlineMembers().isEmpty())
                        end(false);
                    ScoreboardEditor.UpdateTeamsInfected(scoreboard, infected, survivors);
                }
            });
        }

        @EventHandler
        public void onPlayerRespawn(PlayerRespawnEvent e){
            Player player = e.getPlayer();
            if(infected.getMembers().contains(player))
                TeamHandler.respawn(e, infected);

        }

    }
}

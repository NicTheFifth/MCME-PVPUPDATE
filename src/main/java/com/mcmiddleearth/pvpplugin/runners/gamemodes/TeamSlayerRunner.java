package com.mcmiddleearth.pvpplugin.runners.gamemodes;

import com.mcmiddleearth.command.Style;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONMap;
import com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes.JSONTeamSlayer;
import com.mcmiddleearth.pvpplugin.json.transcribers.AreaTranscriber;
import com.mcmiddleearth.pvpplugin.json.transcribers.LocationTranscriber;
import com.mcmiddleearth.pvpplugin.runners.gamemodes.abstractions.GamemodeRunner;
import com.mcmiddleearth.pvpplugin.runners.gamemodes.abstractions.ScoreGoal;
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
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.mcmiddleearth.pvpplugin.command.CommandUtil.sendBaseComponent;

public class TeamSlayerRunner extends GamemodeRunner implements ScoreGoal {
    TSTeam redTeam = new TSTeam();
    TSTeam blueTeam = new TSTeam();
    int scoreGoal;

    public static int DefaultScoreGoal(){
        return 20;
    }

    public TeamSlayerRunner(JSONMap map, int scoreGoal){
        region = AreaTranscriber.TranscribeArea(map);
        this.scoreGoal = scoreGoal;
        JSONTeamSlayer teamSlayer = map.getJSONTeamSlayer();
        maxPlayers = teamSlayer.getMaximumPlayers();
        mapName = map.getTitle();
        eventListener = new TSListener();
        initTeams(map);
        initStartConditions();
        initStartActions();
        initEndActions();
        initJoinConditions();
        initJoinActions();
        initLeaveActions();
        ChatUtils.AnnounceNewGame("Team Slayer", mapName, String.valueOf(maxPlayers));
    }
    //<editor-fold defaultstate="collapsed" desc="Teams">
    public void initTeams(@NotNull JSONMap map){
        initRedTeam(map.getJSONTeamSlayer());
        initBlueTeam(map.getJSONTeamSlayer());
        initSpectator(map.getSpawn());
    }
    private void initRedTeam(@NotNull JSONTeamSlayer teamSlayer){
        redTeam.setPrefix("Red");
        redTeam.setTeamColour(Color.RED);
        redTeam.setChatColor(ChatColor.RED);
        redTeam.setKit(createKit(Color.RED));
        redTeam.setSpawnLocations(
            teamSlayer.getRedSpawns()
                .stream().map(LocationTranscriber::TranscribeFromJSON)
                .collect(Collectors.toList()));
        redTeam.setGameMode(GameMode.ADVENTURE);
    }
    public void initBlueTeam(@NotNull JSONTeamSlayer teamSlayer){
        blueTeam.setPrefix("Blue");
        blueTeam.setTeamColour(Color.BLUE);
        blueTeam.setChatColor(ChatColor.BLUE);
        blueTeam.setKit(createKit(blueTeam.getTeamColour()));
        blueTeam.setSpawnLocations(
            teamSlayer.getBlueSpawns()
                .stream().map(LocationTranscriber::TranscribeFromJSON)
                .collect(Collectors.toList()));
        blueTeam.setGameMode(GameMode.ADVENTURE);
    }
    private @NotNull Kit createKit(Color color){
        Consumer<Player> invFunc = (x -> {
            PlayerInventory returnInventory = x.getInventory();
            returnInventory.clear();
            returnInventory.setHelmet(new ItemStack(Material.LEATHER_HELMET));
            returnInventory.setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
            returnInventory.setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
            returnInventory.setBoots(new ItemStack(Material.LEATHER_BOOTS));
            returnInventory.setItemInOffHand(new ItemStack(Material.SHIELD));
            returnInventory.setItem(0, new ItemStack(Material.IRON_SWORD));
            ItemStack bow = new ItemStack(Material.BOW);
            bow.addEnchantment(Enchantment.INFINITY, 1);
            returnInventory.setItem(1, bow);
            returnInventory.setItem(2, new ItemStack(Material.ARROW));
            returnInventory.forEach(item -> KitEditor.setItemColour(item,
                color));
        });
        return new Kit(invFunc);
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Start conditions">
    protected void initStartConditions() {
        Supplier<Integer> totalInTeams = () ->
            redTeam.getOnlineMembers().size() + blueTeam.getOnlineMembers().size();
        startConditions.put(() -> totalInTeams.get() != players.size() || !redTeam.getOnlineMembers().isEmpty(),
            new ComponentBuilder("Can't start, red team has to have at least " +
                "one online player.")
                .color(Style.ERROR).create());
        startConditions.put(() -> totalInTeams.get() != players.size() ||!blueTeam.getOnlineMembers().isEmpty(),
            new ComponentBuilder("Can't start, blue team has to have at least" +
                " one online player.")
                .color(Style.ERROR).create());
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Start actions">
    protected void initStartActions() {
        startActions.add(() -> players.forEach(player -> JoinTeamSlayer(player, true)));
        startActions.add(()-> ScoreboardEditor.InitTeamSlayer(scoreboard,
            scoreGoal));
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="End actions">
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
            if(redTeam.getPoints() == scoreGoal)
                players.forEach(player ->
                sendBaseComponent(
                    new ComponentBuilder("Red Won!!!").color(ChatColor.RED)
                        .create(), player)) ;
            else
                players.forEach(player ->
                sendBaseComponent(
                    new ComponentBuilder("Blue Won!!!").color(ChatColor.BLUE)
                        .create(), player));});
        endActions.get(false).add(() ->
            PlayerRespawnEvent.getHandlerList().unregister(eventListener));
        endActions.get(true).add(()->
            PlayerRespawnEvent.getHandlerList().unregister(eventListener));
    }

    private Set<Player> getLosingTeamMembers() {
        if(redTeam.getPoints() == scoreGoal)
            return blueTeam.getMembers();
        return redTeam.getMembers();
    }

    private Set<Player> getWinningTeamMembers() {
        if(redTeam.getPoints() == scoreGoal)
            return redTeam.getMembers();
        return blueTeam.getMembers();
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="End conditions">
    private boolean hasEmptyTeam(){
        return redTeam.getOnlineMembers().isEmpty() ||
               blueTeam.getOnlineMembers().isEmpty();
    }
    private boolean isScoreGoalReached(){
        return redTeam.getPoints() >= scoreGoal ||
               blueTeam.getPoints() >= scoreGoal;
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Join">
    @Override
    protected void initJoinConditions() {
        joinConditions.put(((player) ->
                redTeam.getPoints() <=(scoreGoal *0.9) ||
                    blueTeam.getPoints() <=(scoreGoal *0.9)),
            new ComponentBuilder("The game is close to over, you cannot join.")
                .color(Style.INFO)
                .create());
    }

    @Override
    protected void initJoinActions(){
        joinActions.add(player -> JoinTeamSlayer(player, false));
    }

    private void JoinTeamSlayer(Player player, boolean onStart){
        if(!onStart && gameState == State.QUEUED) {
            sendBaseComponent(
                new ComponentBuilder("You joined the game.").color(Style.INFO).create(),
                player);
            return;
        }
        if(redTeam.getMembers().contains(player)) {
            joinRedTeam(player);
            return;
        }
        if(blueTeam.getMembers().contains(player)) {
            joinBlueTeam(player);
            return;
        }
        TeamHandler.addToTeam((team -> team.getOnlineMembers().size()),
            Pair.of(redTeam, () -> joinRedTeam(player)),
            Pair.of(blueTeam, () -> joinBlueTeam(player)));

    }
    private void joinRedTeam(Player player){
        redTeam.getOnlineMembers().add(player);
        Matchmaker.addMember(player, redTeam);
        TeamHandler.spawn(player, redTeam);
        BaseComponent[] publicJoinMessage = new ComponentBuilder(
            String.format("%s has joined the red team!", player.getName()))
            .color(redTeam.getChatColor()).create();
        players.forEach(playerOther ->
            sendBaseComponent(publicJoinMessage, playerOther));
        spectator.getMembers().forEach(spectator ->
            sendBaseComponent(publicJoinMessage, spectator));
    }
    private void joinBlueTeam(Player player){
        blueTeam.getOnlineMembers().add(player);
        Matchmaker.addMember(player, blueTeam);
        TeamHandler.spawn(player, blueTeam);
        BaseComponent[] publicJoinMessage = new ComponentBuilder(
            String.format("%s has joined the blue team!", player.getName()))
            .color(blueTeam.getChatColor()).create();
        players.forEach(playerOther ->
            sendBaseComponent(publicJoinMessage, playerOther));
        spectator.getMembers().forEach(spectator ->
            sendBaseComponent(publicJoinMessage, spectator));
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Leave">
    @Override
    protected void initLeaveActions() {
        leaveActions.add(this::leaveTeamSlayer);
    }
    private void leaveTeamSlayer(Player player){
        if (redTeam.getMembers().contains(player)) {
            leaveRedTeam(player);
        } else {
            leaveBlueTeam(player);
        }
        if(hasEmptyTeam())
            end(true);
    }

    private void leaveRedTeam(Player player){
        redTeam.getOnlineMembers().remove(player);
        players.forEach(playerOther->sendBaseComponent(
            new ComponentBuilder(String.format("%s has left the game.",
                player.getName()))
                .color(redTeam.getChatColor()).create(),
            playerOther
        ));

    }
    private void leaveBlueTeam(Player player){
        blueTeam.getOnlineMembers().remove(player);
        players.forEach(playerOther->sendBaseComponent(
            new ComponentBuilder(String.format("%s has left the game.",
                player.getName()))
                .color(blueTeam.getChatColor()).create(),
            playerOther
        ));
    }
    //</editor-fold>

    public int getScoreGoal(){return scoreGoal;}

    public void setScoreGoal(int scoreGoal){
        this.scoreGoal = scoreGoal;
    }

    public String getGamemode(){return Gamemodes.TEAMSLAYER;}

    private class TSListener extends GamemodeListener{
        public TSListener(){
            initOnPlayerDeathActions();
        }
        @Override
        protected void initOnPlayerDeathActions() {
            onPlayerDeathActions.add(e ->{
                Player player = e.getEntity();
                if(redTeam.getMembers().contains(player))
                    blueTeam.points += 1;
                else
                    redTeam.points += 1;
                ScoreboardEditor.updateValueTeamSlayer(scoreboard,redTeam,blueTeam);
                if(isScoreGoalReached())
                    end(false);
            });
        }
        @EventHandler
        public void onPlayerRespawn(PlayerRespawnEvent e){
            Player player = e.getPlayer();
            if(redTeam.getMembers().contains(player))
                TeamHandler.respawn(e, redTeam);
            if(blueTeam.getMembers().contains(player))
                TeamHandler.respawn(e, blueTeam);
        }
    }
    public static class TSTeam extends Team{
        int points = 0;

        public int getPoints() {
            return points;
        }
    }
}

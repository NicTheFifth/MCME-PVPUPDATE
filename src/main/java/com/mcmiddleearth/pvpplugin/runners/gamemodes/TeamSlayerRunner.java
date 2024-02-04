package com.mcmiddleearth.pvpplugin.runners.gamemodes;

import com.mcmiddleearth.command.Style;
import com.mcmiddleearth.pvpplugin.exceptions.InvalidTeamSlayerException;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONMap;
import com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes.JSONTeamSlayer;
import com.mcmiddleearth.pvpplugin.json.transcribers.AreaTranscriber;
import com.mcmiddleearth.pvpplugin.json.transcribers.LocationTranscriber;
import com.mcmiddleearth.pvpplugin.runners.gamemodes.abstractions.GamemodeRunner;
import com.mcmiddleearth.pvpplugin.runners.runnerUtil.KitEditor;
import com.mcmiddleearth.pvpplugin.runners.runnerUtil.TeamHandler;
import com.mcmiddleearth.pvpplugin.runners.runnerUtil.Validator;
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
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.mcmiddleearth.pvpplugin.command.CommandUtil.sendBaseComponent;

public class TeamSlayerRunner extends GamemodeRunner {
    TSTeam redTeam = new TSTeam();
    TSTeam blueTeam = new TSTeam();
    int scoreGoal;

    public TeamSlayerRunner(JSONMap map, int scoreGoal) throws InvalidTeamSlayerException{
        //TODO: make validator part of requirement commandTree
        if(!Validator.canRunTeamSlayer(map))
            throw new InvalidTeamSlayerException(map);
        region = AreaTranscriber.TranscribeArea(map);
        this.scoreGoal = scoreGoal;
        JSONTeamSlayer teamSlayer = map.getJSONTeamSlayer();
        maxPlayers = teamSlayer.getMaximumPlayers();
        initTeams(map);
        initStartConditions();
        initStartActions();
        initEndActions();
        initJoinConditions();
        initJoinActions();
        initLeaveActions();
    }
    //<editor-fold defaultstate="collapsed" desc="Teams">
    public void initTeams(JSONMap map){
        initRedTeam(map.getJSONTeamSlayer());
        initBlueTeam(map.getJSONTeamSlayer());
        initSpectator(map.getSpawn());
    }
    private void initRedTeam(JSONTeamSlayer teamSlayer){
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
    public void initBlueTeam(JSONTeamSlayer teamSlayer){
        blueTeam.setPrefix("Blue");
        blueTeam.setTeamColour(Color.BLUE);
        blueTeam.setChatColor(ChatColor.BLUE);
        blueTeam.setKit(createKit(Color.BLUE));
        blueTeam.setSpawnLocations(
            teamSlayer.getBlueSpawns()
                .stream().map(LocationTranscriber::TranscribeFromJSON)
                .collect(Collectors.toList()));
        blueTeam.setGameMode(GameMode.ADVENTURE);
    }
    private Kit createKit(Color color){
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
            bow.addEnchantment(Enchantment.ARROW_INFINITE, 1);
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
        startConditions.put(() -> !redTeam.getOnlineMembers().isEmpty(),
            new ComponentBuilder("Can't start, red team has to have at least " +
                "one online player.")
                .color(Style.ERROR).create());
        startConditions.put(() -> !blueTeam.getOnlineMembers().isEmpty(),
            new ComponentBuilder("Can't start, blue team has to have at least" +
                " one online player.")
                .color(Style.ERROR).create());
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Start actions">
    protected void initStartActions() {
        //TODO:Match make
        startActions.add(() ->
            TeamHandler.spawnAll(redTeam, blueTeam, spectator));
        //PVPPlugin.addEventListener(new TeamSlayerListener());
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
    }
    private Set<Player> getLosingTeamMembers() {
        if(redTeam.getPoints() == scoreGoal)
            return blueTeam.getOnlineMembers();
        return redTeam.getOnlineMembers();
    }
    private Set<Player> getWinningTeamMembers() {
        if(redTeam.getPoints() == scoreGoal)
            return redTeam.getOnlineMembers();
        return blueTeam.getOnlineMembers();
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="End conditions">
    private boolean emptyTeam(){
        return redTeam.getOnlineMembers().isEmpty() ||
               blueTeam.getOnlineMembers().isEmpty();
    }
    private boolean scoreGoalReached(){
        return redTeam.getPoints() >= scoreGoal ||
               blueTeam.getPoints() >= scoreGoal;
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Join">
    @Override
    protected void initJoinConditions() {
    }
    @Override
    protected void initJoinActions(){
        joinActions.add(this::join);
    }
    private void join(Player player){
        if(redTeam.getMembers().contains(player)) {
            joinRedTeam(player);
            return;
        }
        if(blueTeam.getMembers().contains(player)) {
            joinBlueTeam(player);
            return;
        }
        TeamHandler.addToTeam((team -> ((TSTeam)team).getOnlineMembers().size()),
            Pair.of(redTeam, () -> joinRedTeam(player)),
            Pair.of(blueTeam, () -> joinBlueTeam(player)));

    }
    private void joinRedTeam(Player player){
        redTeam.getOnlineMembers().add(player);
        Matchmaker.addMember(player, redTeam);
        TeamHandler.spawn(player, redTeam);
        BaseComponent[] joinMessage = new ComponentBuilder("You have joined " +
            "the red team.")
            .color(redTeam.getChatColor()).create();
        sendBaseComponent(joinMessage, player);
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
        BaseComponent[] joinMessage = new ComponentBuilder("You have joined " +
            "the blue team.")
            .color(blueTeam.getChatColor()).create();
        sendBaseComponent(joinMessage, player);
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
        if(emptyTeam())
            end(false);
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
    static int getDefaultScoreGoal(){
        return 20;
    }

    private static class TSTeam extends Team{
        Set<Player> onlineMembers = new HashSet<>();
        int points = 0;

        public int getPoints() {
            return points;
        }

        public Set<Player> getOnlineMembers() {
            return onlineMembers;
        }
    }
}

package com.mcmiddleearth.pvpplugin.runners.gamemodes;

import com.mcmiddleearth.command.Style;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONLocation;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONMap;
import com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes.JSONTeamDeathMatch;
import com.mcmiddleearth.pvpplugin.json.transcribers.AreaTranscriber;
import com.mcmiddleearth.pvpplugin.json.transcribers.LocationTranscriber;
import com.mcmiddleearth.pvpplugin.runners.gamemodes.abstractions.GamemodeRunner;
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
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.mcmiddleearth.pvpplugin.command.CommandUtil.sendBaseComponent;

public class TeamDeathmatchRunner extends GamemodeRunner {
    TDMTeam redTeam = new TDMTeam();
    TDMTeam blueTeam = new TDMTeam();
    public TeamDeathmatchRunner(JSONMap map){
        region = AreaTranscriber.TranscribeArea(map);
        JSONTeamDeathMatch teamDeathMatch = map.getJSONTeamDeathMatch();
        maxPlayers = teamDeathMatch.getMaximumPlayers();
        mapName = map.getTitle();
        eventListener = new TeamDeathmatchRunner.TDMListener();
        initTeams(map);
        initStartConditions();
        initStartActions();
        initEndActions();
        initJoinConditions();
        initJoinActions();
        initLeaveActions();
    }
    //<editor-fold defaultstate="collapsed" desc="Teams">
    private void initTeams(JSONMap map){
        initTeamBlue(map.getJSONTeamDeathMatch().getBlueSpawn());
        initTeamRed(map.getJSONTeamDeathMatch().getRedSpawn());
        initSpectator(map.getSpawn());
    }
    private void initTeamBlue(JSONLocation blueSpawn){
        redTeam.setPrefix("Blue");
        redTeam.setTeamColour(Color.BLUE);
        redTeam.setChatColor(ChatColor.BLUE);
        redTeam.setKit(createKit(Color.BLUE));
        blueTeam.setSpawnLocations(
            List.of(LocationTranscriber.TranscribeFromJSON(blueSpawn)));
        blueTeam.setGameMode(GameMode.ADVENTURE);
    }
    private void initTeamRed(JSONLocation redSpawn){
        redTeam.setPrefix("Red");
        redTeam.setTeamColour(Color.RED);
        redTeam.setChatColor(ChatColor.RED);
        redTeam.setKit(createKit(Color.RED));
        redTeam.setSpawnLocations(
            List.of(LocationTranscriber.TranscribeFromJSON(redSpawn)));
        redTeam.setGameMode(GameMode.ADVENTURE);

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
            bow.addEnchantment(Enchantment.ARROW_INFINITE, 1);
            returnInventory.setItem(1, bow);
            returnInventory.setItem(2, new ItemStack(Material.ARROW));
            returnInventory.forEach(item -> KitEditor.setItemColour(item,
                color));
        });
        return new Kit(invFunc);
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Start conditions"
    @Override
    protected void initStartConditions() {
        Supplier<Integer> totalInTeams = () ->
            redTeam.onlineMembers.size() + blueTeam.getOnlineMembers().size();
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
    //<editor-fold defaultstate="collapsed" desc="Start actions"
    @Override
    protected void initStartActions() {
        startActions.add(() -> players.forEach(this::join));
        startActions.add(()-> ScoreboardEditor.InitTeamDeathmatch(scoreboard,
            redTeam, blueTeam
            ));
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
            if(redTeam.hasAliveMembers()) {
                players.forEach(player ->
                    sendBaseComponent(
                        new ComponentBuilder("Red Won!!!").color(ChatColor.RED)
                            .create(), player));
                spectator.getMembers().forEach(player ->
                    sendBaseComponent(
                        new ComponentBuilder("Red Won!!!").color(ChatColor.RED)
                            .create(), player));
            }
            else{
                players.forEach(player ->
                    sendBaseComponent(
                        new ComponentBuilder("Blue Won!!!").color(ChatColor.BLUE)
                            .create(), player));
                spectator.getMembers().forEach(player ->
                    sendBaseComponent(
                        new ComponentBuilder("Blue Won!!!").color(ChatColor.BLUE)
                            .create(), player));}});
        endActions.get(false).add(() ->
            PlayerRespawnEvent.getHandlerList().unregister(eventListener));
        endActions.get(true).add(()->
            PlayerRespawnEvent.getHandlerList().unregister(eventListener));
    }
    private Set<Player> getLosingTeamMembers() {
        if(redTeam.hasAliveMembers())
            return blueTeam.getMembers();
        return redTeam.getMembers();
    }
    private Set<Player> getWinningTeamMembers() {
        if(redTeam.hasAliveMembers())
            return redTeam.getMembers();
        return blueTeam.getMembers();
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Join">
    @Override
    protected void initJoinConditions() {
        joinConditions.put(((player) ->
                redTeam.AliveMembers() >= 3 || blueTeam.AliveMembers() >= 3),
            new ComponentBuilder("The game is close to over, you cannot join.")
                .color(Style.INFO)
                .create());
    }
    @Override
    protected void initJoinActions() {
        joinActions.add(this::join);
    }
    private void join(Player player){
        if(gameState == State.QUEUED) {
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
        TeamHandler.addToTeam((team -> ((TeamSlayerRunner.TSTeam)team).getOnlineMembers().size()),
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
        leaveActions.add(this::leaveTeamDeathmatch);
    }
    private void leaveTeamDeathmatch(Player player){
        if (redTeam.getMembers().contains(player)) {
            leaveRedTeam(player);
        } else {
            leaveBlueTeam(player);
        }
        if(!blueTeam.hasAliveMembers() || !redTeam.hasAliveMembers()) {
            end(true);
        }
        ScoreboardEditor.updateValueTeamDeathmatch(scoreboard, redTeam,blueTeam);
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
    @Override
    public String getGamemode() {
        return Gamemodes.TEAMDEATHMATCH;
    }
    public class TDMListener extends GamemodeListener{
        @Override
        protected void initOnPlayerDeathActions() {
            onPlayerDeathActions.add(e ->{
                Player player = e.getEntity();
                if(redTeam.getMembers().contains(player))
                    redTeam.deadMembers.add(player);
                else
                    blueTeam.deadMembers.add(player);
                ScoreboardEditor.updateValueTeamDeathmatch(scoreboard,redTeam,blueTeam);
                if(!redTeam.hasAliveMembers() || !blueTeam.hasAliveMembers())
                    end(false);
            });
        }
        @EventHandler
        public void onPlayerRespawn(PlayerRespawnEvent e){
            TeamHandler.respawn(e, spectator);
        }
    }
    public static class TDMTeam extends Team {
        Set<Player> onlineMembers = new HashSet<>();
        Set<Player> deadMembers = new HashSet<>();
        public boolean hasAliveMembers(){
            return deadMembers.containsAll(onlineMembers);
        }
        public Set<Player> getOnlineMembers() {
            return onlineMembers;
        }
        public Integer AliveMembers(){
            Set<Player> aliveMembers = new HashSet<>(onlineMembers);
            aliveMembers.removeAll(deadMembers);
            return aliveMembers.size();
        }
    }
}
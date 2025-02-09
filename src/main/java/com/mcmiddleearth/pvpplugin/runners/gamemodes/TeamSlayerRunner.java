package com.mcmiddleearth.pvpplugin.runners.gamemodes;

import com.mcmiddleearth.pvpplugin.PVPPlugin;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONMap;
import com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes.JSONTeamSlayer;
import com.mcmiddleearth.pvpplugin.json.transcribers.AreaTranscriber;
import com.mcmiddleearth.pvpplugin.json.transcribers.LocationTranscriber;
import com.mcmiddleearth.pvpplugin.runners.gamemodes.abstractions.GamemodeRunner;
import com.mcmiddleearth.pvpplugin.runners.gamemodes.abstractions.ScoreGoal;
import com.mcmiddleearth.pvpplugin.runners.runnerUtil.KitEditor;
import com.mcmiddleearth.pvpplugin.runners.runnerUtil.ScoreboardEditor;
import com.mcmiddleearth.pvpplugin.runners.runnerUtil.TeamHandler;
import com.mcmiddleearth.pvpplugin.statics.Gamemodes;
import com.mcmiddleearth.pvpplugin.util.Kit;
import com.mcmiddleearth.pvpplugin.util.Matchmaker;
import com.mcmiddleearth.pvpplugin.util.PlayerStatEditor;
import com.mcmiddleearth.pvpplugin.util.Team;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static net.kyori.adventure.text.format.NamedTextColor.BLUE;
import static net.kyori.adventure.text.format.NamedTextColor.RED;

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
        redTeam.setChatColor(RED);
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
        blueTeam.setChatColor(BLUE);
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
            returnInventory.forEach(KitEditor::setUnbreaking);
        });
        return new Kit(invFunc);
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Start conditions">
    protected void initStartConditions() {
        Supplier<Integer> totalInTeams = () ->
                redTeam.getOnlineMembers().size() + blueTeam.getOnlineMembers().size();
        startConditions.put(() -> totalInTeams.get() != players.size() || !redTeam.getOnlineMembers().isEmpty(),
                mm.deserialize("<red>Can't start, red team has to have at least one online player.</red>"));
        startConditions.put(() -> totalInTeams.get() != players.size() ||!blueTeam.getOnlineMembers().isEmpty(),
                mm.deserialize("<red>Can't start, blue team has to have at least one online player.</red>"));
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
                PVPPlugin.getInstance().sendMessage(mm.deserialize("<red>Red won!!!</red>"));
            else
                PVPPlugin.getInstance().sendMessage(mm.deserialize("<blue>Blue won!!!</blue>"));});
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
                mm.deserialize("<red>The game is close to over, you cannot join.</red>"));
    }

    @Override
    protected void initJoinActions(){
        joinActions.add(player -> JoinTeamSlayer(player, false));
    }

    private void JoinTeamSlayer(Player player, boolean onStart){
        if(!onStart && gameState == State.QUEUED) {
            player.sendMessage(mm.deserialize("<aqua>You joined the game.</aqua>"));
            return;
        }
        if(redTeam.getMembers().contains(player)) {
            joinTeam(player, redTeam);
            return;
        }
        if(blueTeam.getMembers().contains(player)) {
            joinTeam(player, blueTeam);
            return;
        }
        TeamHandler.addToTeam((team -> team.getOnlineMembers().size()),
            Pair.of(redTeam, () -> joinTeam(player, redTeam)),
            Pair.of(blueTeam, () -> joinTeam(player, blueTeam)));

    }

    private void joinTeam(Player player, TSTeam team) {
        team.getOnlineMembers().add(player);
        Matchmaker.addMember(player, team);
        TeamHandler.spawn(player, team);
        PVPPlugin.getInstance().sendMessage(
                String.format("<%s>%s has joined the %s</%s>",
                        team.getChatColor(),
                        player.getName(),
                        team.getPrefix(),
                        team.getChatColor()));
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Leave">
    @Override
    protected void initLeaveActions() {
        leaveActions.add(this::leaveTeamSlayer);
    }
    private void leaveTeamSlayer(Player player){
        if (redTeam.getMembers().contains(player)) {
            leaveTeam(player, redTeam);
        } else {
            leaveTeam(player, blueTeam);
        }
        if(hasEmptyTeam())
            end(true);
    }

    private void leaveTeam(Player player, Team team){
        team.getOnlineMembers().remove(player);
        PVPPlugin.getInstance().sendMessage(
                String.format("<%s>%s has left the game.</%s>",
                        team.getChatColor(),
                        player.getName(),
                        team.getChatColor()));
    }
    //</editor-fold>

    public Boolean trySendMessage(Player player, Function<List<TagResolver>, Component> messageBuilder){
        if(!players.contains(player))
            return false;
        Team team = null;
        if(redTeam.getMembers().contains(player))
            team = redTeam;
        if(blueTeam.getMembers().contains(player))
            team=blueTeam;
        if(team == null)
            return false;

        PVPPlugin.getInstance().sendMessage(messageBuilder.apply(
                List.of(Placeholder.parsed("prefix", team.getPrefix()),
                        Placeholder.styling("color", team.getChatColor()))));
        return true;
    }

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
            if(gameState != State.RUNNING)
                return;
            if(redTeam.getMembers().contains(player))
                TeamHandler.respawn(e, redTeam);
            if(blueTeam.getMembers().contains(player))
                TeamHandler.respawn(e, blueTeam);
        }

        @EventHandler
        public void onPlayerDamage(EntityDamageByEntityEvent e){
            if(!(e.getEntity() instanceof Player player))
                return;
            if(!(e.getDamager() instanceof Player damager))
                return;
            if((redTeam.getMembers().contains(player) && redTeam.getMembers().contains(damager)) ||
                    (blueTeam.getMembers().contains(player) && blueTeam.getMembers().contains(damager)))
                e.setCancelled(true);
        }
    }
    public static class TSTeam extends Team{
        int points = 0;

        public int getPoints() {
            return points;
        }
    }
}

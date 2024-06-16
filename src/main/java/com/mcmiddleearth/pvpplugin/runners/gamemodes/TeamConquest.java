package com.mcmiddleearth.pvpplugin.runners.gamemodes;

import com.mcmiddleearth.command.Style;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONMap;
import com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes.JSONTeamConquest;
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
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.mcmiddleearth.pvpplugin.command.CommandUtil.sendBaseComponent;

public class TeamConquest extends GamemodeRunner {

    TCTeam blueTeam = new TCTeam();
    TCTeam redTeam = new TCTeam();

    int scoreGoal;
    public static int DefaultScoreGoal(){
        return 20;
    }

    //0 neutral, -50 = blueTeam, 50 = redTeam
    HashMap<Location, Integer> capturePointsProgress = new HashMap<>();

    public TeamConquest(JSONMap map, int scoreGoal){
        region = AreaTranscriber.TranscribeArea(map);
        this.scoreGoal = scoreGoal;
        JSONTeamConquest teamConquest = map.getJSONTeamConquest();
        teamConquest.getCapturePoints().stream().map(LocationTranscriber::TranscribeFromJSON).forEach(location ->
                this.capturePointsProgress.put(location, 0));
        maxPlayers = teamConquest.getMaximumPlayers();
        mapName = map.getTitle();
        eventListener = new TCListener();
        initTeams(map);
        initPoints();
        initStartConditions();
        initStartActions();
        initEndActions();
        initJoinConditions();
        initJoinActions();
        initLeaveActions();
    }
    //<editor-fold defaultstate="collapsed" desc="Teams">
    private void initTeams(JSONMap map){
        initBlueTeam(map.getJSONTeamConquest());
        initRedTeam(map.getJSONTeamConquest());
        initSpectator(map.getSpawn());
    }


    private void initBlueTeam(JSONTeamConquest jsonTeamConquest){
        blueTeam.setPrefix("Blue");
        blueTeam.setTeamColour(Color.BLUE);
        blueTeam.setChatColor(ChatColor.BLUE);
        blueTeam.setGameMode(GameMode.ADVENTURE);
        blueTeam.setKit(createKit(Color.BLUE));
        blueTeam.setSpawnLocations(List.of(LocationTranscriber.TranscribeFromJSON(jsonTeamConquest.getBlueSpawn())));
    }

    private void initRedTeam(JSONTeamConquest jsonTeamConquest){
        redTeam.setPrefix("Red");
        redTeam.setChatColor(ChatColor.RED);
        redTeam.setTeamColour(Color.RED);
        redTeam.setGameMode(GameMode.ADVENTURE);
        redTeam.setKit(createKit(Color.RED));
        redTeam.setSpawnLocations(List.of(LocationTranscriber.TranscribeFromJSON(jsonTeamConquest.getRedSpawn())));

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
    //<editor-fold defaultstate="collapsed" desc="Capture points">
    private void initPoints(){
        capturePointsProgress.keySet().forEach(this::spawnCapturePoint);
    }

    private void spawnCapturePoint(Location capturePoint){
        capturePoint.getBlock().setType(Material.BEACON);

        capturePoint.getBlock().getRelative(0, -1, -1).setType(Material.IRON_BLOCK);
        capturePoint.getBlock().getRelative(0, -1, 0).setType(Material.IRON_BLOCK);
        capturePoint.getBlock().getRelative(0, -1, 1).setType(Material.IRON_BLOCK);
        capturePoint.getBlock().getRelative(1, -1, -1).setType(Material.IRON_BLOCK);
        capturePoint.getBlock().getRelative(1, -1, 0).setType(Material.IRON_BLOCK);
        capturePoint.getBlock().getRelative(1, -1, 1).setType(Material.IRON_BLOCK);
        capturePoint.getBlock().getRelative(-1, -1, -1).setType(Material.IRON_BLOCK);
        capturePoint.getBlock().getRelative(-1, -1, 0).setType(Material.IRON_BLOCK);
        capturePoint.getBlock().getRelative(-1, -1, 1).setType(Material.IRON_BLOCK);
    }

    private void deleteCapturePoint(Location capturePoint){
        capturePoint.getBlock().setType(Material.AIR);
        capturePoint.getBlock().getRelative(0, 1, 0).setType(Material.AIR);
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Start Conditions">
    @Override
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
    //<editor-fold defaultstate="collapsed" desc="Start Actions">
    @Override
    protected void initStartActions() {
        startActions.add(() -> players.forEach(this::join));
        startActions.add(()-> ScoreboardEditor.InitTeamConquest(scoreboard,
                scoreGoal));
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
                capturePointsProgress.keySet().forEach(this::deleteCapturePoint));
        endActions.get(true).add(() ->
                capturePointsProgress.keySet().forEach(this::deleteCapturePoint));
        endActions.get(false).add(() ->{
                    PlayerRespawnEvent.getHandlerList().unregister(eventListener);
                    PlayerInteractEvent.getHandlerList().unregister(eventListener);
        });
        endActions.get(true).add(()->{
            PlayerRespawnEvent.getHandlerList().unregister(eventListener);
            PlayerInteractEvent.getHandlerList().unregister(eventListener);
        });
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
    //<editor-fold defaultstate="collapsed" desc="End Conditions">
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
        TeamHandler.addToTeam((team -> team.getOnlineMembers().size()),
                Pair.of(redTeam, () -> joinRedTeam(player)),
                Pair.of(blueTeam, () -> joinBlueTeam(player)));
    }

    private void joinBlueTeam(Player player) {
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

    private void joinRedTeam(Player player) {
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
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Leave">
    @Override
    protected void initLeaveActions() {
        leaveActions.add(this::leave);
    }

    private void leave(Player player){
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

    @Override
    public String getGamemode() {
        return Gamemodes.TEAMCONQUEST;
    }

    public class TCListener extends GamemodeListener{
        public TCListener(){
            initOnPlayerDeathActions();
        }

        @Override
        protected void initOnPlayerDeathActions() {
            onPlayerDeathActions.add(e ->{
                Player player = e.getEntity();
                int redPoints = redTeam.controlledPoints - blueTeam.controlledPoints;
                if(redTeam.getMembers().contains(player))
                    blueTeam.addPoints(Math.max(-redPoints, 0));
                else
                    redTeam.addPoints(Math.max(redPoints, 0));
                ScoreboardEditor.updateValueTeamConquest(scoreboard,redTeam,blueTeam);
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

        @EventHandler
        public void onPlayerInteract(PlayerInteractEvent e) {
            Player p = e.getPlayer();
            if(!e.getAction().equals(Action.RIGHT_CLICK_BLOCK))
                return;
            if(gameState != State.RUNNING )
                return;
            if(!players.contains(p))
                return;
            if(!e.getClickedBlock().getType().equals(Material.BEACON))
                return;
            e.setUseInteractedBlock(Event.Result.DENY);
            Location capturePoint = e.getClickedBlock().getLocation();
            int oldPointValue = capturePointsProgress.get(capturePoint);
            int newPointValue= oldPointValue;
            if(redTeam.getMembers().contains(p))
                newPointValue += 1;
            else
                newPointValue -= 1;
            if(Math.abs(newPointValue) > 50)
                return;
            switch(newPointValue){
                case 0:
                    //TODO: If not claimed, don't remove controlled point
                    capturePoint.add(0,1,0).getBlock().setType(Material.AIR);
                    if(oldPointValue < 0)
                        blueTeam.removeControlledPoint();
                    else
                        redTeam.removeControlledPoint();
                    sendBaseComponent(new ComponentBuilder("Point is neutral!").color(ChatColor.GRAY).create(),p);
                    break;

                case 50:
                    redTeam.addControlledPoint();
                    capturePoint.add(0,1,0).getBlock().setType(Material.RED_STAINED_GLASS);
                    players.forEach(player ->
                            sendBaseComponent(new ComponentBuilder("Red team captured a point!")
                                        .color(redTeam.getChatColor())
                                        .create(),
                                    player));
                    spectator.getMembers().forEach(player ->
                            sendBaseComponent(new ComponentBuilder("Red team captured a point!")
                                            .color(redTeam.getChatColor())
                                            .create(),
                                    player));
                    break;
                case -50:
                    blueTeam.addControlledPoint();
                    capturePoint.add(0,1,0).getBlock().setType(Material.BLUE_STAINED_GLASS);
                    players.forEach(player ->
                            sendBaseComponent(new ComponentBuilder("Blue team captured a point!")
                                            .color(blueTeam.getChatColor())
                                            .create(),
                                    player));
                    spectator.getMembers().forEach(player ->
                            sendBaseComponent(new ComponentBuilder("Blue team captured a point!")
                                            .color(blueTeam.getChatColor())
                                            .create(),
                                    player));
                    break;
            }
            sendBaseComponent(new ComponentBuilder(String.format("Point is now at %d!", newPointValue)).color(ChatColor.GRAY).create(),p);
            capturePointsProgress.put(capturePoint, newPointValue);
        }
    }

    public static class TCTeam extends Team{
        private int points = 0;

        private int controlledPoints = 0;

        public void addPoints(int pointsToAdd) {
            points += pointsToAdd;
        }

        public int getPoints(){
            return points;
        }

        public void addControlledPoint(){
            controlledPoints+=1;
        }

        public void removeControlledPoint(){
            controlledPoints -=1;
        }

        public int getControlledPoints(){
            return controlledPoints;
        }
    }
}

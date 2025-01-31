package com.mcmiddleearth.pvpplugin.runners.gamemodes;

import com.mcmiddleearth.command.Style;
import com.mcmiddleearth.pvpplugin.PVPPlugin;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONMap;
import com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes.JSONCaptureTheFlag;
import com.mcmiddleearth.pvpplugin.json.transcribers.AreaTranscriber;
import com.mcmiddleearth.pvpplugin.json.transcribers.LocationTranscriber;
import com.mcmiddleearth.pvpplugin.runners.gamemodes.abstractions.GamemodeRunner;
import com.mcmiddleearth.pvpplugin.runners.gamemodes.abstractions.ScoreGoal;
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
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.mcmiddleearth.pvpplugin.command.CommandUtil.sendBaseComponent;

public class CaptureTheFlagRunner extends GamemodeRunner implements ScoreGoal, TimeLimit {

    int scoreGoal;
    int timeLimit;

    public static int GetDefaultScoreGoal(){
        return 5;
    }
    public static int GetDefaultTimeLimit(){
        return 300;
    }

    AtomicBoolean suddenDeath = new AtomicBoolean(false);

    CTFTeam redTeam = new CTFTeam();
    CTFTeam blueTeam = new CTFTeam();

    public CaptureTheFlagRunner(JSONMap map, int scoreGoal, int timeLimit){
        region = AreaTranscriber.TranscribeArea(map);
        this.scoreGoal = scoreGoal;
        this.timeLimit = timeLimit;
        JSONCaptureTheFlag captureTheFlag = map.getJSONCaptureTheFlag();
        maxPlayers = captureTheFlag.getMaximumPlayers();
        mapName = map.getTitle();
        eventListener = new CTFListener();
        initTeams(map);
        initFlags();
        initStartConditions();
        initStartActions();
        initEndActions();
        initJoinConditions();
        initJoinActions();
        initLeaveActions();
        ChatUtils.AnnounceNewGame("Capture the Flag", mapName, String.valueOf(maxPlayers));
    }

    private void initTeams(JSONMap map){
        initSpectator(map.getSpawn());
        initBlueTeam(map.getJSONCaptureTheFlag());
        initRedTeam(map.getJSONCaptureTheFlag());
    }

    private void initBlueTeam(JSONCaptureTheFlag jsonCaptureTheFlag) {
        blueTeam.setPrefix("Blue");
        blueTeam.setTeamColour(Color.BLUE);
        blueTeam.setChatColor(NamedTextColor.BLUE);
        blueTeam.setKit(createKit(Color.BLUE));
        blueTeam.setSpawnLocations(jsonCaptureTheFlag.getBlueSpawns().stream()
                .map(LocationTranscriber::TranscribeFromJSON).collect(Collectors.toList()));
        blueTeam.setGameMode(GameMode.SURVIVAL);
        blueTeam.setFlag(LocationTranscriber.TranscribeFromJSON(jsonCaptureTheFlag.getBlueFlag()).add(0,1,0));
        blueTeam.setFlagMaterial(Material.BLUE_BANNER);
    }

    private void initRedTeam(JSONCaptureTheFlag jsonCaptureTheFlag) {
        redTeam.setPrefix("Red");
        redTeam.setTeamColour(Color.RED);
        redTeam.setChatColor(NamedTextColor.RED);
        redTeam.setKit(createKit(Color.RED));
        redTeam.setSpawnLocations(jsonCaptureTheFlag.getRedSpawns().stream()
                .map(LocationTranscriber::TranscribeFromJSON).collect(Collectors.toList()));
        redTeam.setGameMode(GameMode.SURVIVAL);
        redTeam.setFlag(LocationTranscriber.TranscribeFromJSON(jsonCaptureTheFlag.getRedFlag()).add(0,1,0));
        redTeam.setFlagMaterial(Material.RED_BANNER);
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

    private void initFlags(){
        redTeam.getFlag().getBlock().setType(redTeam.getFlagMaterial());
        redTeam.getFlag().getBlock().getRelative(0, 2, 0).setType(Material.RED_STAINED_GLASS);
        initFlagBeacon(redTeam.getFlag().getBlock().getRelative(0,-1,0));
        blueTeam.getFlag().getBlock().setType(blueTeam.getFlagMaterial());
        blueTeam.getFlag().getBlock().getRelative(0, 2, 0).setType(Material.BLUE_STAINED_GLASS);
        initFlagBeacon(blueTeam.getFlag().getBlock().getRelative(0,-1,0));
    }

    private void initFlagBeacon(Block beaconLocation){

        beaconLocation.setType(Material.BEACON);
        beaconLocation.getRelative(0, -1, -1).setType(Material.IRON_BLOCK);
        beaconLocation.getRelative(0, -1, 0).setType(Material.IRON_BLOCK);
        beaconLocation.getRelative(0, -1, 1).setType(Material.IRON_BLOCK);
        beaconLocation.getRelative(1, -1, -1).setType(Material.IRON_BLOCK);
        beaconLocation.getRelative(1, -1, 0).setType(Material.IRON_BLOCK);
        beaconLocation.getRelative(1, -1, 1).setType(Material.IRON_BLOCK);
        beaconLocation.getRelative(-1, -1, -1).setType(Material.IRON_BLOCK);
        beaconLocation.getRelative(-1, -1, 0).setType(Material.IRON_BLOCK);
        beaconLocation.getRelative(-1, -1, 1).setType(Material.IRON_BLOCK);
    }

    @Override
    protected void initStartConditions() {
        Supplier<Integer> totalInTeams = () ->
                redTeam.getOnlineMembers().size() + blueTeam.getOnlineMembers().size();
        startConditions.put(() ->
                        totalInTeams.get() != players.size() || !redTeam.getOnlineMembers().isEmpty(),
                new ComponentBuilder("Can't start, red team has to have at least " +
                        "one online player.")
                        .color(Style.ERROR).create());
        startConditions.put(() ->
                        totalInTeams.get() != players.size() ||!blueTeam.getOnlineMembers().isEmpty(),
                new ComponentBuilder("Can't start, blue team has to have at least" +
                        " one online player.")
                        .color(Style.ERROR).create());
    }

    @Override
    protected void initStartActions() {
        startActions.add(() -> players.forEach(player -> JoinCaptureTheFlag(player, true)));
        startActions.add(()-> ScoreboardEditor.InitCaptureTheFlag(scoreboard, scoreGoal, timeLimit));
        startActions.add(() -> new BukkitRunnable() {
            @Override
            public void run() {
                if (gameState == State.ENDED){
                    this.cancel();
                    return;
                }
                if(gameState == State.COUNTDOWN)
                {
                    return;
                }
                if (timeLimit == 0) {
                    if(redTeam.getPoints() == blueTeam.getPoints()){
                        suddenDeath.getAndSet(true);
                        players.forEach (player -> sendBaseComponent(new ComponentBuilder("Sudden death, the next to score wins!!!").create(), player));
                    } else {
                        end(false);
                        gameState = State.ENDED;
                    }
                    this.cancel();
                    return;
                }
                timeLimit--;
                ScoreboardEditor.UpdateTimeCaptureTheFlag(scoreboard, timeLimit);
            }
        }.runTaskTimer(PVPPlugin.getInstance(),100,20));
    }

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
        endActions.get(false).add(() -> {
            PlayerRespawnEvent.getHandlerList().unregister(eventListener);
            PlayerInteractEvent.getHandlerList().unregister(eventListener);
        });
        endActions.get(true).add(()->{
            PlayerRespawnEvent.getHandlerList().unregister(eventListener);
            PlayerInteractEvent.getHandlerList().unregister(eventListener);
        });
        endActions.get(true).add(() -> {
            redTeam.getFlag().getBlock().setType(Material.AIR);
            blueTeam.getFlag().getBlock().setType(Material.AIR);
            blueTeam.getFlag().getBlock().getRelative(0, 2, 0).setType(Material.AIR);
            blueTeam.getFlag().getBlock().getRelative(0, -1, 0).setType(Material.AIR);
            redTeam.getFlag().getBlock().getRelative(0, 2, 0).setType(Material.AIR);
            redTeam.getFlag().getBlock().getRelative(0, -1, 0).setType(Material.AIR);});
        endActions.get(false).add(()-> {
            redTeam.getFlag().getBlock().setType(Material.AIR);
            blueTeam.getFlag().getBlock().setType(Material.AIR);
            blueTeam.getFlag().getBlock().getRelative(0, 2, 0).setType(Material.AIR);
            blueTeam.getFlag().getBlock().getRelative(0, -1, 0).setType(Material.AIR);
            redTeam.getFlag().getBlock().getRelative(0, 2, 0).setType(Material.AIR);
            redTeam.getFlag().getBlock().getRelative(0, -1, 0).setType(Material.AIR);});
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
        joinActions.add(player -> JoinCaptureTheFlag(player, false));
    }

    private void JoinCaptureTheFlag(Player player, boolean onStart){
        if(!onStart && gameState == State.QUEUED) {
            sendBaseComponent(
                    new ComponentBuilder("You joined the game.").color(Style.INFO).create(),
                    player);
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

    private void joinTeam(Player player, CTFTeam team){
        team.getOnlineMembers().add(player);
        Matchmaker.addMember(player, team);
        TeamHandler.spawn(player, team);
        PVPPlugin.getInstance().sendMessage(
                String.format("<%s>%s has joined the %s team!</%s>",
                        team.getChatColor(),
                        player.getName(),
                        team.getPrefix(),
                        team.getChatColor()));
    }

    @Override
    protected void initLeaveActions() {
        leaveActions.add(this::LeaveCaptureTheFlag);
    }

    private void LeaveCaptureTheFlag(Player player){
        if (redTeam.getMembers().contains(player)) {
            leaveTeam(player, redTeam.getChatColor());
            redTeam.getOnlineMembers().remove(player);
        } else {
            leaveTeam(player,blueTeam.getChatColor());
            blueTeam.getOnlineMembers().remove(player);
        }
        if(hasEmptyTeam())
            end(true);
    }

    private void leaveTeam(Player player, NamedTextColor color){
        PVPPlugin.getInstance().sendMessage(
                String.format("<%s>%s has left the game.</%s>",
                        color,
                        player.getName(),
                        color));
    }

    @Override
    public Boolean trySendMessage(Player player, Function<List<TagResolver>, Component> messageBuilder){
        if(!players.contains(player))
            return false;
        CTFTeam team = null;
        if(blueTeam.getMembers().contains(player)){
            team = blueTeam;
        }
        if(redTeam.getMembers().contains(player)){
            team=redTeam;
        }
        if(team == null)
            return false;

        PVPPlugin.getInstance().sendMessage(messageBuilder.apply(
                List.of(Placeholder.parsed("prefix", team.getPrefix()),
                        Placeholder.styling("color", team.getChatColor()))));
        return true;
    }

    @Override
    public String getGamemode() {
        return Gamemodes.CAPTURETHEFLAG;
    }

    @Override
    public int getScoreGoal() {
        return scoreGoal;
    }

    @Override
    public void setScoreGoal(int scoreGoal) {
        this.scoreGoal = scoreGoal;
    }

    @Override
    public int getTimeLimit() {
        return timeLimit;
    }

    @Override
    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    private class CTFListener extends GamemodeListener{
        public CTFListener(){
            initOnPlayerDeathActions();
        }
        @Override
        protected void initOnPlayerDeathActions() {
            onPlayerDeathActions.add(e -> {
                Player player = e.getEntity();
                if(player.getInventory().getHelmet() == null)
                    return;
                if(Objects.equals(player.getInventory().getHelmet().getType(), blueTeam.getFlagMaterial())) {
                    players.forEach(playerOther -> sendBaseComponent(new ComponentBuilder("Red team has dropped blue's flag.").create(), playerOther));
                    spectator.getMembers().forEach(playerOther -> sendBaseComponent(new ComponentBuilder("Red team has dropped blue's flag.").create(), playerOther));
                    blueTeam.getFlag().getBlock().setType(blueTeam.getFlagMaterial());
                    redTeam.getKit().getInventory().accept(player);
                }
                if(Objects.equals(player.getInventory().getHelmet().getType(), redTeam.getFlagMaterial())) {
                    players.forEach(playerOther -> sendBaseComponent(new ComponentBuilder("Blue team has dropped red's flag.").create(), playerOther));
                    spectator.getMembers().forEach(playerOther -> sendBaseComponent(new ComponentBuilder("Blue team has dropped red's flag.").create(), playerOther));
                    redTeam.getFlag().getBlock().setType(redTeam.getFlagMaterial());
                    blueTeam.getKit().getInventory().accept(player);
                }
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
        public void onPlayerInteract(PlayerInteractEvent e){
            Player player = e.getPlayer();
            Block block = e.getClickedBlock();
            if(block == null)
                return;
            if(!e.getAction().equals(Action.RIGHT_CLICK_BLOCK))
                return;
            if(gameState != State.RUNNING )
                return;
            if(!players.contains(player))
                return;
            if(!block.getLocation().equals(redTeam.getFlag()) && !block.getLocation().equals(blueTeam.getFlag()))
                return;
            if(redTeam.getMembers().contains(player) && Objects.equals(block.getType(), blueTeam.getFlagMaterial())){
                player.getInventory().setHelmet(new ItemStack(blueTeam.getFlagMaterial()));
                blueTeam.getFlag().getBlock().setType(Material.AIR);
                Consumer<Player> message = playerOther -> sendBaseComponent(new ComponentBuilder(String.format("%s has taken blue's flag!", player.getName())).create(), playerOther);
                players.forEach(message);
                spectator.getMembers().forEach(message);
            }
            if(blueTeam.getMembers().contains(player) && Objects.equals(block.getType(), redTeam.getFlagMaterial())){
                player.getInventory().setHelmet(new ItemStack(redTeam.getFlagMaterial()));
                redTeam.getFlag().getBlock().setType(Material.AIR);
                Consumer<Player> message = playerOther -> sendBaseComponent(new ComponentBuilder(String.format("%s has taken red's flag!", player.getName())).create(), playerOther);
                players.forEach(message);
                spectator.getMembers().forEach(message);
            }
        }

        @EventHandler
        public void onPlayerMoveCTF(PlayerMoveEvent e){
            Player player = e.getPlayer();
            if(gameState != State.RUNNING )
                return;
            if(!players.contains(player))
                return;
            if(e.getTo() == null)
                return;
            if(player.getInventory().getHelmet() == null)
                return;
            if(Objects.equals(player.getInventory().getHelmet().getType(), blueTeam.getFlagMaterial())){
                if(redTeam.getFlag().distance(e.getTo()) <= 5){
                    redTeam.addPoint();
                    redTeam.getKit().getInventory().accept(player);
                    Consumer<Player> message = otherPlayer -> sendBaseComponent(
                            new ComponentBuilder(String.format("%s scored a point for the red Team!", player.getName())).create(),
                            otherPlayer
                    );
                    players.forEach(message);
                    spectator.getMembers().forEach(message);
                    if(suddenDeath.get()){
                        redTeam.WinSuddenDeath(scoreGoal);
                        end(false);
                        return;
                    }
                    blueTeam.getFlag().getBlock().setType(blueTeam.getFlagMaterial());
                }
            }
            if(Objects.equals(player.getInventory().getHelmet().getType(), redTeam.getFlagMaterial())){
                if(blueTeam.getFlag().distance(e.getTo()) <= 5){
                    blueTeam.addPoint();
                    blueTeam.getKit().getInventory().accept(player);
                    Consumer<Player> message = otherPlayer -> sendBaseComponent(
                            new ComponentBuilder(String.format("%s scored a point for the blue Team!", player.getName())).create(),
                            otherPlayer
                    );
                    players.forEach(message);
                    spectator.getMembers().forEach(message);
                    if(suddenDeath.get()){
                        blueTeam.WinSuddenDeath(scoreGoal);
                        end(false);
                        return;
                    }
                    redTeam.getFlag().getBlock().setType(redTeam.getFlagMaterial());
                }
            }
            if(isScoreGoalReached()) {
                end(false);
                return;
            }
            ScoreboardEditor.UpdatePointsCaptureTheFlag(scoreboard, blueTeam, redTeam);
        }
    }
    public static class CTFTeam extends Team {
        int points = 0;
        Location flag;
        Material flagMaterial;

        public void WinSuddenDeath(int scoreGoal){
            points = scoreGoal;
        }

        public void setFlagMaterial(Material flagMaterial){ this.flagMaterial = flagMaterial;}
        public Material getFlagMaterial(){return flagMaterial;}

        public void setFlag(Location flag){ this.flag = flag;}
        public Location getFlag(){return flag;}

        public void addPoint(){points+=1;}
        public int getPoints(){return points;}
    }
}

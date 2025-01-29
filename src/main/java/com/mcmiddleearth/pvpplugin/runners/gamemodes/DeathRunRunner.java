package com.mcmiddleearth.pvpplugin.runners.gamemodes;

import com.mcmiddleearth.command.Style;
import com.mcmiddleearth.pvpplugin.PVPPlugin;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONLocation;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONMap;
import com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes.JSONDeathRun;
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
import net.kyori.adventure.text.format.NamedTextColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.mcmiddleearth.pvpplugin.command.CommandUtil.sendBaseComponent;

public class DeathRunRunner extends GamemodeRunner implements TimeLimit {
    int timeLimit;

    public static int DefaultTimeLimit(){return 120;}

    final int killHeight;
    final Location goal;

    final Team death = new Team();
    final DRTeam runner = new DRTeam();

    public DeathRunRunner(JSONMap map, int timeLimit){
        region = AreaTranscriber.TranscribeArea(map);
        JSONDeathRun deathRun = map.getJSONDeathRun();
        this.timeLimit = timeLimit;
        killHeight = deathRun.getKillHeight();
        goal = LocationTranscriber.TranscribeFromJSON(deathRun.getGoal());
        maxPlayers = deathRun.getMaximumPlayers();
        mapName = map.getTitle();
        eventListener = new DRListener();
        initGoal();
        initTeams(map);
        initStartConditions();
        initStartActions();
        initEndActions();
        initJoinConditions();
        initJoinActions();
        initLeaveActions();
        ChatUtils.AnnounceNewGame("Death Run", mapName, String.valueOf(maxPlayers));
    }
    public void initGoal(){
        goal.getBlock().setType(Material.BEACON);
    }
    public void initTeams(JSONMap map){
        initSpectator(map.getSpawn());
        initDeath(map.getJSONDeathRun().getDeathSpawn());
        initRunner(map.getJSONDeathRun().getRunnerSpawn());
    }

    private void initRunner(JSONLocation runnerSpawn) {
        runner.setPrefix("Runner");
        runner.setTeamColour(Color.BLUE);
        runner.setChatColor(NamedTextColor.BLUE);
        runner.setGameMode(GameMode.SURVIVAL);
        runner.setKit(new Kit(player -> {
            PlayerInventory returnInventory = player.getInventory();
            returnInventory.clear();}));
        runner.setSpawnLocations(List.of(LocationTranscriber.TranscribeFromJSON(runnerSpawn)));
    }

    private void initDeath(JSONLocation deathSpawn) {
        death.setPrefix("Death");
        death.setTeamColour(Color.BLACK);
        death.setChatColor(NamedTextColor.BLACK);
        death.setGameMode(GameMode.SURVIVAL);
        death.setKit(DeathKit());
        death.setSpawnLocations(List.of(LocationTranscriber.TranscribeFromJSON(deathSpawn)));
    }

    private Kit DeathKit(){
        Consumer<Player> invFunc = (player -> {
            PlayerInventory returnInventory = player.getInventory();
            returnInventory.clear();
            returnInventory.setHelmet(new ItemStack(Material.WITHER_SKELETON_SKULL));
            ItemStack bow = new ItemStack(Material.BOW);
            bow.addEnchantment(Enchantment.ARROW_INFINITE, 1);
            returnInventory.setItem(1, bow);
            returnInventory.setItem(2, new ItemStack(Material.ARROW));
            returnInventory.forEach(KitEditor::setUnbreaking);
        });
        return new Kit(invFunc);
    }

    @Override
    protected void initStartConditions() {
        Supplier<Integer> totalInTeams = () ->
                death.getOnlineMembers().size() + runner.getOnlineMembers().size();
        startConditions.put(() -> totalInTeams.get() != players.size() || !runner.getOnlineMembers().isEmpty(),
                new ComponentBuilder("Can't start, runner has to have at least " +
                        "one online player.")
                        .color(Style.ERROR).create());
        startConditions.put(() -> totalInTeams.get() != players.size() ||!death.getOnlineMembers().isEmpty(),
                new ComponentBuilder("Can't start, death has to have at least" +
                        " one online player.")
                        .color(Style.ERROR).create());
    }

    @Override
    protected void initStartActions() {
        startActions.add(() -> players.forEach(player -> {
            initWithRandomDeath();
            JoinDeathRun(player, true);
        }));
        startActions.add(()-> ScoreboardEditor.InitDeathRun(scoreboard, timeLimit, runner));
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
                if (gameState == State.ENDED){
                    this.cancel();
                    return;
                }
                timeLimit--;
                ScoreboardEditor.UpdateTimeDeathRun(scoreboard, timeLimit);
            }
        }.runTaskTimer(PVPPlugin.getInstance(),100,20));
    }

    private void initWithRandomDeath() {
        if (!death.getOnlineMembers().isEmpty())
            return;
        Player player;
        do {
            int randomInfectedIndex = ThreadLocalRandom.current().nextInt(players.size());
            player = (Player) players.toArray()[randomInfectedIndex];
        } while (runner.getMembers().contains(player));
        death.getMembers().add(player);
    }

    @Override
    protected void initEndActions() {
        endActions.get(false).add(() ->{
                if(runner.getFinished().isEmpty()) {
                    death.getMembers().forEach(PlayerStatEditor::addWon);
                    runner.getMembers().forEach(PlayerStatEditor::addLost);
                    players.forEach(player ->
                            sendBaseComponent(
                                    new ComponentBuilder("Death Won!!!")
                                            .create(), player));
                    spectator.getMembers().forEach(player ->
                            sendBaseComponent(
                                    new ComponentBuilder("Death Won!!!")
                                            .create(), player));
                } else{
                    runner.getFinished().forEach(PlayerStatEditor::addWon);
                    runner.getDeadMembers().forEach(PlayerStatEditor::addLost);
                    death.getMembers().forEach(PlayerStatEditor::addLost);
                    Consumer<Player> message = player -> sendBaseComponent(
                            new ComponentBuilder(
                                    String.format("%s has won!",
                                            runner.getFinished().stream().map(Player::getName).collect(Collectors.joining(", ")))).create(),
                            player);
                    players.forEach(message);
                    spectator.getMembers().forEach(message);
                }});
        endActions.get(false).add(() -> {
            PlayerRespawnEvent.getHandlerList().unregister(eventListener);
            PlayerInteractEvent.getHandlerList().unregister(eventListener);
            goal.getBlock().setType(Material.AIR);
        });
        endActions.get(true).add(() -> {
            PlayerRespawnEvent.getHandlerList().unregister(eventListener);
            PlayerInteractEvent.getHandlerList().unregister(eventListener);
            goal.getBlock().setType(Material.AIR);
        });
    }

    @Override
    protected void initJoinConditions() {
        joinConditions.put((player ->
                        gameState == State.QUEUED || timeLimit <= 60),
                new ComponentBuilder("The game is close to over, you cannot join.")
                        .color(Style.INFO)
                        .create());
    }

    @Override
    protected void initJoinActions() {
        joinActions.add(player -> JoinDeathRun(player, false));
    }

    private void JoinDeathRun(Player player, boolean onStart){
        if(!onStart && gameState == State.QUEUED) {
            sendBaseComponent(
                    new ComponentBuilder("You joined the game.").color(Style.INFO).create(),
                    player);
            return;
        }
        if(death.getMembers().contains(player)){
            death.getOnlineMembers().add(player);
            Matchmaker.addMember(player, death);
            TeamHandler.spawn(player, death);
            BaseComponent[] publicJoinMessage = new ComponentBuilder(
                    String.format("%s has joined the deaths!", player.getName())).create();
            players.forEach(playerOther ->
                    sendBaseComponent(publicJoinMessage, playerOther));
            spectator.getMembers().forEach(spectator ->
                    sendBaseComponent(publicJoinMessage, spectator));
            return;
        }
        runner.getOnlineMembers().add(player);
        Matchmaker.addMember(player, runner);
        if(runner.getDeadMembers().contains(player)) {
            TeamHandler.spawn(player, spectator);
            sendBaseComponent(
                    new ComponentBuilder("You've joined the runners, but were already dead.")
                            .color(Style.INFO)
                            .create(),
                    player);
            return;
        }
        if(runner.getFinished().contains(player)) {
            TeamHandler.spawn(player, spectator);
            sendBaseComponent(
                    new ComponentBuilder("You've joined the runners, but had already finished.")
                            .color(Style.INFO)
                            .create(),
                    player);
            return;
        }
        TeamHandler.spawn(player, runner);
        BaseComponent[] publicJoinMessage = new ComponentBuilder(
                String.format("%s has joined the runners!", player.getName())).create();
        players.forEach(playerOther ->
                sendBaseComponent(publicJoinMessage, playerOther));
        spectator.getMembers().forEach(spectator ->
                sendBaseComponent(publicJoinMessage, spectator));

    }

    @Override
    protected void initLeaveActions() {
        leaveActions.add(this::LeaveDeathRun);
    }

    public void LeaveDeathRun(Player player){
        Consumer<Player> leaveMessage = playerOther ->sendBaseComponent(
                new ComponentBuilder(String.format("%s has left the game.",
                        player.getName())).create(),
                playerOther
        );
        if (death.getMembers().contains(player)) {
            death.getOnlineMembers().remove(player);
        } else {
            runner.getOnlineMembers().remove(player);
        }
        players.forEach(leaveMessage);
        spectator.getMembers().forEach(leaveMessage);
        if(death.getOnlineMembers().isEmpty())
            end(true);
        if(runner.getOnlineMembers().isEmpty())
            end(false);
    }

    @Override
    public Boolean trySendSpectatorMessage(Player player, String message){
        return trySendMessage(player, message);
    }

    public Boolean trySendMessage(Player player, String message){
        if(!players.contains(player))
            return false;

        Set<Player> deads = new HashSet<>(runner.getDeadMembers());
        deads.addAll(spectator.getMembers());
        deads.addAll(runner.finished);
        String prefix = null;
        if(runner.getDeadMembers().contains(player)){
            prefix = "Dead Runner";
        }
        if(runner.finished.contains(player))
            prefix = "Finished Runner";
        if(spectator.getMembers().contains(player))
            prefix = "Spectator";
        if(prefix != null){
            PVPPlugin.getInstance().sendMessageTo(
                    String.format("<gray>%s %s:</gray> %s",
                            prefix,
                            player.getDisplayName(),
                            message),
                    deads);
            return true;
        }
        Team team = null;
        if(death.getMembers().contains(player))
            team = death;
        if(runner.getMembers().contains(player))
            team = runner;
        if(team == null)
            return false;

        PVPPlugin.getInstance().sendMessage(
                String.format("<%s>%s %s:</%s> %s",
                        team.getChatColor(),
                        team.getPrefix(),
                        player.getDisplayName(),
                        team.getChatColor(),
                        message));
        return true;
    }

    @Override
    public String getGamemode() {
        return Gamemodes.DEATHRUN;
    }

    @Override
    public int getTimeLimit() {
        return timeLimit;
    }

    @Override
    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    private class DRListener extends GamemodeListener{
        public DRListener(){
            initOnPlayerDeathActions();
        }

        @Override
        protected void initOnPlayerDeathActions() {
            onPlayerDeathActions.add(e -> {
                Player player = e.getEntity();
                if(runner.getOnlineMembers().remove(player)) {
                    runner.getDeadMembers().add(player);
                    if(runner.getOnlineMembers().isEmpty()) {
                        end(false);
                        return;
                    }
                    ScoreboardEditor.UpdateRunnersDeathRun(scoreboard, runner);
                }
            });
        }

        @EventHandler
        public void onPlayerRespawn(PlayerRespawnEvent e){
            Player player = e.getPlayer();
            if(death.getMembers().contains(player))
                TeamHandler.respawn(e, death);
            else
                TeamHandler.respawn(e, spectator);
        }

        @EventHandler
        public void onPlayerMoveDR(PlayerMoveEvent e){
            Player player = e.getPlayer();
            if(!players.contains(player))
                return;
            if(spectator.getMembers().contains(player))
                return;
            if(e.getTo() != null && e.getTo().getBlockY() <=killHeight)
                player.setHealth(0);

        }

        @EventHandler
        public void onPlayerInteract(PlayerInteractEvent e){
            Player player = e.getPlayer();
            if(!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                return;
            }
            if(gameState != State.RUNNING ) {
                return;
            }
            if(!players.contains(player)) {
                return;
            }
            if(e.getClickedBlock().getType() != Material.BEACON)
                return;
            Block possibleGoal = e.getClickedBlock();
            if(!possibleGoal.equals(goal.getBlock()))
                return;
            if(death.getMembers().contains(player))
                return;
            runner.getOnlineMembers().remove(player);
            runner.getFinished().add(player);
            TeamHandler.spawn(player, spectator);
            if(runner.getOnlineMembers().isEmpty())
                end(false);
            e.setUseInteractedBlock(Event.Result.DENY);
        }
    }

    public static class DRTeam extends Team{
        Set<Player> deadMembers = new HashSet<>();
        Set<Player> finished = new HashSet<>();

        public Set<Player> getDeadMembers(){
            return deadMembers;
        }
        public Set<Player> getFinished() {
            return finished;
        }
    }
}

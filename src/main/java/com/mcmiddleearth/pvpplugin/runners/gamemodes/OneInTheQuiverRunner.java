package com.mcmiddleearth.pvpplugin.runners.gamemodes;

import com.mcmiddleearth.command.Style;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONMap;
import com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes.JSONOneInTheQuiver;
import com.mcmiddleearth.pvpplugin.json.transcribers.AreaTranscriber;
import com.mcmiddleearth.pvpplugin.json.transcribers.LocationTranscriber;
import com.mcmiddleearth.pvpplugin.runners.gamemodes.abstractions.GamemodeRunner;
import com.mcmiddleearth.pvpplugin.runners.gamemodes.abstractions.ScoreGoal;
import com.mcmiddleearth.pvpplugin.runners.runnerUtil.ScoreboardEditor;
import com.mcmiddleearth.pvpplugin.runners.runnerUtil.TeamHandler;
import com.mcmiddleearth.pvpplugin.statics.Gamemodes;
import com.mcmiddleearth.pvpplugin.util.PlayerStatEditor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.mcmiddleearth.pvpplugin.command.CommandUtil.sendBaseComponent;

public class OneInTheQuiverRunner extends GamemodeRunner implements ScoreGoal {

    private int scoreGoal;
    public static int defaultScoreGoal = 20;

    private final List<Location> spawns;
    private final Map<Player, PlayerTeam> OITQplayers = new HashMap<>();

    private Player winningPlayer;

    public OneInTheQuiverRunner(JSONMap map, int scoreGoal){
        region = AreaTranscriber.TranscribeArea(map);
        JSONOneInTheQuiver oneInTheQuiver = map.getJSONOneInTheQuiver();
        this.scoreGoal = scoreGoal;
        maxPlayers = oneInTheQuiver.getMaximumPlayers();
        mapName = map.getTitle();
        eventListener = new OITQListener();
        this.spawns = oneInTheQuiver.getSpawns().stream().map(LocationTranscriber::TranscribeFromJSON).collect(Collectors.toList());
        initStartConditions();
        initStartActions();
        initEndActions();
        initJoinConditions();
        initJoinActions();
        initLeaveActions();

    }

    @Override
    protected void initStartConditions() {
        startConditions.put(() -> players.size() >= 2,
                new ComponentBuilder("Cannot start a game unless it has two or more players.")
                        .color(Style.ERROR).create());
    }

    @Override
    protected void initStartActions() {
        startActions.add(() -> players.forEach(this::join));
        startActions.add(()-> ScoreboardEditor.InitOneInTheQuiver(scoreboard, OITQplayers, scoreGoal));
    }

    @Override
    protected void initEndActions() {
        endActions.get(false).add(() -> players.forEach(player -> {
            if (player == winningPlayer) {
                PlayerStatEditor.addWon(player);
            } else {
                PlayerStatEditor.addLost(player);
            }
            sendBaseComponent(new ComponentBuilder(winningPlayer.getDisplayName() + " has won!")
                    .color(OITQplayers.get(winningPlayer).chatColor.asBungee()).create(),
                    player);
        }));
        endActions.get(false).add(() -> {
            PlayerRespawnEvent.getHandlerList().unregister(eventListener);
            EntityShootBowEvent.getHandlerList().unregister(eventListener);
        });
        endActions.get(true).add(() -> {
            PlayerRespawnEvent.getHandlerList().unregister(eventListener);
            EntityShootBowEvent.getHandlerList().unregister(eventListener);
        });
    }

    @Override
    protected void initJoinConditions() {
        joinConditions.put((player -> OITQplayers.values().stream().noneMatch(playerTeam -> playerTeam.getKills() <= (scoreGoal * 0.9))),
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

        ChatColor color = OITQplayers.getOrDefault(player, GenerateNewPlayer(player)).getChatColor();
        KitOutPlayer(player);
        player.setGameMode(GameMode.ADVENTURE);
        TeamHandler.spawn(player, spawns);

        BaseComponent[] joinMessage = new ComponentBuilder(player.getDisplayName() + " has joined the game!")
                .color(color.asBungee()).create();
        players.forEach(playerOther -> sendBaseComponent(joinMessage, playerOther));
        spectator.getMembers().forEach(spectator -> sendBaseComponent(joinMessage, spectator));
    }

    private PlayerTeam GenerateNewPlayer(Player player){
        PlayerTeam playerTeam = new PlayerTeam();
        playerTeam.setChatColor(ChatColor.values()[OITQplayers.size() % 16]);
        OITQplayers.put(player, playerTeam);
        return playerTeam;
    }

    private void KitOutPlayer(Player player){
        PlayerInventory playerInventory = player.getInventory();
        playerInventory.clear();
        playerInventory.setHelmet(new ItemStack(Material.LEATHER_HELMET));
        playerInventory.setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
        playerInventory.setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
        playerInventory.setBoots(new ItemStack(Material.LEATHER_BOOTS));
        playerInventory.setItem(0, new ItemStack(Material.IRON_AXE));
        playerInventory.setItem(1, new ItemStack(Material.BOW));
        playerInventory.setItem(2, new ItemStack(Material.ARROW));
    }

    @Override
    protected void initLeaveActions() {
        leaveActions.add(this::leave);
    }
    private void leave(Player player){
        if(players.size() <= 1)
            end(true);
    }

    @Override
    public String getGamemode() {
        return Gamemodes.ONEINTHEQUIVER;
    }

    @Override
    public int getScoreGoal() {
        return scoreGoal;
    }

    @Override
    public void setScoreGoal(int scoreGoal) {
        this.scoreGoal = scoreGoal;

    }

    public class OITQListener extends GamemodeListener{
        public OITQListener(){
            initOnPlayerDeathActions();
        }

        @Override
        protected void initOnPlayerDeathActions() {
            onPlayerDeathActions.add(e -> {
                Player player = e.getEntity();
                if(!players.contains(player))
                    return;
                Player killer = player.getKiller();
                if(killer == null)
                    return;
                killer.getInventory().addItem(new ItemStack(Material.ARROW, 1));
                PlayerTeam killerTeam = OITQplayers.get(killer);
                killerTeam.addKill();
                ScoreboardEditor.UpdateOneInTheQuiver(scoreboard, killer, killerTeam);
                if(killerTeam.getKills() >= scoreGoal) {
                    winningPlayer = killer;
                    end(false);
                }
            });
        }

        @EventHandler
        public void onPlayerRespawn(PlayerRespawnEvent e){
            Player player = e.getPlayer();
            if(!players.contains(player))
                return;
            if(!player.getInventory().contains(Material.ARROW))
                player.getInventory().addItem(new ItemStack(Material.ARROW,1));
            TeamHandler.respawn(e, spawns);
        }

        @EventHandler
        public void onEntityShootBowEvent(EntityShootBowEvent e){
            if(!(e.getEntity() instanceof Player))
                return;
            if(!(e.getProjectile() instanceof Arrow))
                return;
            Arrow arrow = (Arrow)e.getProjectile();
            Player player = (Player) e.getEntity();
            if(!(players.contains(player)))
                return;
            arrow.setDamage(100);
        }
    }

    public class PlayerTeam {
        int kills = 0;
        ChatColor chatColor;

        public void addKill(){
            kills++;
            if(kills >= scoreGoal)
                end(false);
        }
        public void setChatColor(ChatColor chatColor){
            this.chatColor = chatColor;
        }
        public ChatColor getChatColor(){return chatColor;}
        public int getKills() {return kills;}
    }
}

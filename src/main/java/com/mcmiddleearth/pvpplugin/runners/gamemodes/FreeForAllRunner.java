package com.mcmiddleearth.pvpplugin.runners.gamemodes;

import com.mcmiddleearth.command.Style;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONMap;
import com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes.JSONFreeForAll;
import com.mcmiddleearth.pvpplugin.json.transcribers.AreaTranscriber;
import com.mcmiddleearth.pvpplugin.json.transcribers.LocationTranscriber;
import com.mcmiddleearth.pvpplugin.runners.gamemodes.abstractions.GamemodeRunner;
import com.mcmiddleearth.pvpplugin.runners.runnerUtil.ChatUtils;
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
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import com.mcmiddleearth.pvpplugin.PVPPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.mcmiddleearth.pvpplugin.command.CommandUtil.sendBaseComponent;

public class FreeForAllRunner extends GamemodeRunner {
    private final List<Location> spawns;
    private final Map<Player, PlayerTeam> FFAplayers = new HashMap<>();
    int timeLimitSeconds;
    public static int DefaultTimeLimit(){
        return 300;
    }

    public FreeForAllRunner(JSONMap map, int timeLimitSeconds) {
        region = AreaTranscriber.TranscribeArea(map);
        JSONFreeForAll freeForAll = map.getJSONFreeForAll();
        this.timeLimitSeconds = timeLimitSeconds;
        maxPlayers = freeForAll.getMaximumPlayers();
        mapName = map.getTitle();
        eventListener = new FFAListener();
        this.spawns = freeForAll.getSpawns().stream().map(LocationTranscriber::TranscribeFromJSON).collect(Collectors.toList());
        initStartConditions();
        initStartActions();
        initEndActions();
        initJoinConditions();
        initJoinActions();
        initLeaveActions();
        initSpectator(map.getSpawn());
        ChatUtils.AnnounceNewGame("Free for All", mapName, String.valueOf(maxPlayers));
    }

    @Override
    protected void initStartConditions() {
        startConditions.put(() -> players.size() >= 2,
                new ComponentBuilder("Cannot start a game unless it has two or more players.")
                        .color(Style.ERROR).create());
    }
    @Override
    protected void initStartActions() {
        startActions.add(() -> players.forEach(player -> JoinFreeForAll(player, true)));
        startActions.add(()-> ScoreboardEditor.InitFreeForAll(scoreboard, FFAplayers, timeLimitSeconds));
        startActions.add(() -> new BukkitRunnable() {
            @Override
            public void run() {
                if (gameState == State.COUNTDOWN) {
                    return;
                }
                if (timeLimitSeconds == 0) {
                    end(false);
                    gameState = State.ENDED;
                    this.cancel();
                    return;
                }
                timeLimitSeconds--;
                ScoreboardEditor.UpdateTimeFreeForAll(scoreboard, timeLimitSeconds);
            }
        }.runTaskTimer(PVPPlugin.getInstance(), 100, 20));
    }

    @Override
    protected void initEndActions() {
        endActions.get(false).add(() -> {
            Player winningPlayer = FFAplayers.entrySet().stream()
                    .filter(entry -> players.contains(entry.getKey()))
                    .max(Map.Entry.comparingByValue(Comparator.comparingInt(PlayerTeam::getKills)))
                    .map(Map.Entry::getKey).orElse(null);
            players.forEach(player -> {
                if (player == winningPlayer) {
                    PlayerStatEditor.addWon(player);
                } else {
                    PlayerStatEditor.addLost(player);
                }
                sendBaseComponent(new ComponentBuilder(winningPlayer.getName() + " has won!")
                                .color(FFAplayers.get(winningPlayer).chatColor.asBungee()).create(),
                        player);
            });
        });
        endActions.get(false).add(() -> PlayerRespawnEvent.getHandlerList().unregister(eventListener));
        endActions.get(true).add(() -> PlayerRespawnEvent.getHandlerList().unregister(eventListener));
    }

    @Override
    protected void initJoinConditions() {
        joinConditions.put(((player) ->
                        timeLimitSeconds <=60),
                new ComponentBuilder("The game is close to over, you cannot join.")
                        .color(Style.INFO)
                        .create());
    }

    @Override
    protected void initJoinActions() {
        joinActions.add(player -> JoinFreeForAll(player, false));
    }

    private void JoinFreeForAll(Player player, boolean onStart){
        if(!onStart && gameState == State.QUEUED) {
            sendBaseComponent(
                    new ComponentBuilder("You joined the game.").color(Style.INFO).create(),
                    player);
            return;
        }
        ChatColor color = FFAplayers.getOrDefault(player, GenerateNewPlayer(player)).getChatColor();
        KitOutPlayer(player);
        player.setGameMode(GameMode.ADVENTURE);
        TeamHandler.spawn(player, spawns);

        BaseComponent[] joinMessage = new ComponentBuilder(player.getDisplayName() + " has joined the game!")
                .color(color.asBungee()).create();
        players.forEach(playerOther -> sendBaseComponent(joinMessage, playerOther));
        spectator.getMembers().forEach(spectator -> sendBaseComponent(joinMessage, spectator));
    }

    private FreeForAllRunner.PlayerTeam GenerateNewPlayer(Player player){
        FreeForAllRunner.PlayerTeam playerTeam = new FreeForAllRunner.PlayerTeam();
        playerTeam.setChatColor(ChatColor.values()[FFAplayers.size() % 16]);
        FFAplayers.put(player, playerTeam);
        return playerTeam;
    }

    private void KitOutPlayer(Player player){
        PlayerInventory playerInventory = player.getInventory();
        playerInventory.clear();
        playerInventory.setHelmet(new ItemStack(Material.LEATHER_HELMET));
        playerInventory.setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
        playerInventory.setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
        playerInventory.setBoots(new ItemStack(Material.LEATHER_BOOTS));
        playerInventory.setItem(0, new ItemStack(Material.IRON_SWORD));
        ItemStack bow = new ItemStack(Material.BOW);
        bow.addEnchantment(Enchantment.ARROW_INFINITE, 1);
        playerInventory.setItem(1, bow);
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
        return Gamemodes.FREEFORALL;
    }

    public class FFAListener extends GamemodeListener{
        public FFAListener(){
            initOnPlayerDeathActions();
        }

        @Override
        protected void initOnPlayerDeathActions() {
            onPlayerDeathActions.add(e -> {
                Player player = e.getEntity();
                Player killer = player.getKiller();
                if(killer == null)
                    return;
                FreeForAllRunner.PlayerTeam killerTeam = FFAplayers.get(killer);
                killerTeam.addKill();
                ScoreboardEditor.UpdateFreeForAll(scoreboard, killer, killerTeam);
            });
        }

        @EventHandler
        public void onPlayerRespawn(PlayerRespawnEvent e){
            Player player = e.getPlayer();
            if(!players.contains(player))
                return;
            TeamHandler.respawn(e, spawns);
        }
    }

    public static class PlayerTeam {
        int kills = 0;
        ChatColor chatColor;

        public void addKill(){
            kills++;
        }
        public void setChatColor(ChatColor chatColor){
            this.chatColor = chatColor;
        }
        public ChatColor getChatColor(){return chatColor;}
        public int getKills() {return kills;}
    }

}

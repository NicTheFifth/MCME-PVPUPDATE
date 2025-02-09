package com.mcmiddleearth.pvpplugin.runners.gamemodes;

import com.mcmiddleearth.pvpplugin.json.jsonData.JSONMap;
import com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes.JSONFreeForAll;
import com.mcmiddleearth.pvpplugin.json.transcribers.AreaTranscriber;
import com.mcmiddleearth.pvpplugin.json.transcribers.LocationTranscriber;
import com.mcmiddleearth.pvpplugin.runners.gamemodes.abstractions.GamemodeRunner;
import com.mcmiddleearth.pvpplugin.runners.gamemodes.abstractions.TimeLimit;
import com.mcmiddleearth.pvpplugin.runners.runnerUtil.KitEditor;
import com.mcmiddleearth.pvpplugin.runners.runnerUtil.ScoreboardEditor;
import com.mcmiddleearth.pvpplugin.runners.runnerUtil.TeamHandler;
import com.mcmiddleearth.pvpplugin.statics.Gamemodes;
import com.mcmiddleearth.pvpplugin.util.PlayerStatEditor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
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

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FreeForAllRunner extends GamemodeRunner implements TimeLimit {
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
        initStartActions();
        initEndActions();
        initJoinConditions();
        initJoinActions();
        initLeaveActions();
        initSpectator(map.getSpawn());
    }

    @Override
    protected void initStartConditions() {
    }

    @Override
    protected void initStartActions() {
        startActions.add(() -> players.forEach(player -> JoinFreeForAll(player, true)));
        startActions.add(()-> ScoreboardEditor.InitFreeForAll(scoreboard, FFAplayers, timeLimitSeconds));
        startActions.add(() -> new BukkitRunnable() {
            @Override
            public void run() {
                if(gameState == State.ENDED)
                    this.cancel();
                if(gameState == State.COUNTDOWN) {
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
            int maxKills = FFAplayers.entrySet().stream()
                    .filter(entry -> players.contains(entry.getKey()))
                    .max(Map.Entry.comparingByValue(Comparator.comparingInt(PlayerTeam::getKills)))
                    .map(entry -> entry.getValue().getKills()).orElse(0);
            List<Player> winningPlayers = FFAplayers.entrySet().stream()
                    .filter(entry -> players.contains(entry.getKey()) && entry.getValue().getKills() == maxKills)
                    .map(Map.Entry::getKey).toList();
            players.forEach(player -> {
                if (winningPlayers.contains(player)) {
                    PlayerStatEditor.addWon(player);
                } else {
                    PlayerStatEditor.addLost(player);
                }
            });
            PVPPlugin.getInstance().sendMessage(mm.deserialize("<players> won!",
                    Placeholder.parsed("players",winningPlayers.stream().map(Player::getName).collect(Collectors.joining(", ")))));
        });
        endActions.get(false).add(() -> PlayerRespawnEvent.getHandlerList().unregister(eventListener));
        endActions.get(true).add(() -> PlayerRespawnEvent.getHandlerList().unregister(eventListener));
    }

    @Override
    protected void initJoinConditions() {
        joinConditions.put(((player) ->
                    timeLimitSeconds >= 60),
                mm.deserialize("<aqua>The game is close to over, you cannot join.</aqua>"));
    }

    @Override
    protected void initJoinActions() {
        joinActions.add(player -> JoinFreeForAll(player, false));
    }

    private void JoinFreeForAll(Player player, boolean onStart){
        if(!onStart && gameState == State.QUEUED) {
            player.sendMessage(mm.deserialize("<aqua>You joined the game.</aqua>"));
            return;
        }
        NamedTextColor color = FFAplayers.getOrDefault(player, GenerateNewPlayer(player)).getChatColor();
        KitOutPlayer(player);
        player.setGameMode(GameMode.ADVENTURE);
        TeamHandler.spawn(player, spawns);

        PVPPlugin.getInstance().sendMessage(
                String.format("<%s>%s has joined the game!</%s>",
                        color,
                        player.getName(),
                        color));
    }

    private FreeForAllRunner.PlayerTeam GenerateNewPlayer(Player player){
        FreeForAllRunner.PlayerTeam playerTeam = new FreeForAllRunner.PlayerTeam();
        playerTeam.setChatColor((NamedTextColor)NamedTextColor.NAMES.values().toArray()[FFAplayers.size() % 16]);
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
        bow.addEnchantment(Enchantment.INFINITY, 1);
        playerInventory.setItem(1, bow);
        playerInventory.setItem(2, new ItemStack(Material.ARROW));
        playerInventory.forEach(KitEditor::setUnbreaking);
    }

    public Boolean trySendMessage(Player player, Function<List<TagResolver>, Component> messageBuilder){
        if(!players.contains(player))
            return false;
        PlayerTeam team = FFAplayers.get(player);
        if(team != null){
            List<TagResolver> resolvers = new ArrayList<>();
            resolvers.add(Placeholder.parsed("prefix", team.getChatColor().examinableName()));
            resolvers.add(Placeholder.styling("color", team.getChatColor()));
            PVPPlugin.getInstance().sendMessage(messageBuilder.apply(resolvers));
            return true;
        }
        return false;
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

    @Override
    public int getTimeLimit() {
        return timeLimitSeconds;
    }

    @Override
    public void setTimeLimit(int timeLimit) {
        this.timeLimitSeconds=timeLimit;
    }

    private class FFAListener extends GamemodeListener{
        public FFAListener(){
            initOnPlayerDeathActions();
        }

        @Override
        protected void initOnPlayerDeathActions() {
            onPlayerDeathActions.add(e -> {
                Player player = e.getEntity();
                Player killer = player.getKiller();
                if(killer == null) {
                    return;
                }
                FreeForAllRunner.PlayerTeam killerTeam = FFAplayers.get(killer);
                killerTeam.addKill();
                ScoreboardEditor.UpdateFreeForAll(scoreboard, killer, killerTeam);
            });
        }

        @EventHandler
        public void onPlayerRespawn(PlayerRespawnEvent e){
            Player player = e.getPlayer();
            if(gameState != State.RUNNING)
                return;
            if(!players.contains(player))
                return;
            TeamHandler.respawn(e, spawns);
        }
    }

    public static class PlayerTeam {
        int kills = 0;
        NamedTextColor chatColor;

        public void addKill(){
            kills++;
        }
        public void setChatColor(NamedTextColor chatColor){
            this.chatColor = chatColor;
        }
        public NamedTextColor getChatColor(){return chatColor;}
        public int getKills() {return kills;}
    }

}

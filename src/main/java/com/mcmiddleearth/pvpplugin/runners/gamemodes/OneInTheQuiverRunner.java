package com.mcmiddleearth.pvpplugin.runners.gamemodes;

import com.mcmiddleearth.command.Style;
import com.mcmiddleearth.pvpplugin.PVPPlugin;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONMap;
import com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes.JSONOneInTheQuiver;
import com.mcmiddleearth.pvpplugin.json.transcribers.AreaTranscriber;
import com.mcmiddleearth.pvpplugin.json.transcribers.LocationTranscriber;
import com.mcmiddleearth.pvpplugin.runners.gamemodes.abstractions.GamemodeRunner;
import com.mcmiddleearth.pvpplugin.runners.gamemodes.abstractions.ScoreGoal;
import com.mcmiddleearth.pvpplugin.runners.runnerUtil.ChatUtils;
import com.mcmiddleearth.pvpplugin.runners.runnerUtil.KitEditor;
import com.mcmiddleearth.pvpplugin.runners.runnerUtil.ScoreboardEditor;
import com.mcmiddleearth.pvpplugin.runners.runnerUtil.TeamHandler;
import com.mcmiddleearth.pvpplugin.statics.Gamemodes;
import com.mcmiddleearth.pvpplugin.util.PlayerStatEditor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.md_5.bungee.api.chat.ComponentBuilder;
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
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.mcmiddleearth.pvpplugin.command.CommandUtil.sendBaseComponent;

public class OneInTheQuiverRunner extends GamemodeRunner implements ScoreGoal {

    private int scoreGoal;
    public static int DefaultScoreGoal(){return 20;}

    private final List<Location> spawns;
    private final Map<UUID, PlayerTeam> OITQplayers = new HashMap<>();
    //TODO: add random offset to PlayerTeamColours
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
        initSpectator(map.getSpawn());
        ChatUtils.AnnounceNewGame("One in the Quiver", mapName, String.valueOf(maxPlayers));
    }

    @Override
    protected void initStartConditions() {
        startConditions.put(() -> players.size() >= 2,
                new ComponentBuilder("Cannot start a game unless it has two or more players.")
                        .color(Style.ERROR).create());
    }

    @Override
    protected void initStartActions() {
        startActions.add(() -> players.forEach(player -> JoinOneInTheQuiver(player, true)));
        startActions.add(()-> ScoreboardEditor.InitOneInTheQuiver(scoreboard, OITQplayers, scoreGoal));
    }

    @Override
    protected void initEndActions() {
        endActions.get(false).add(() -> players.forEach(player -> {
            if(winningPlayer != null){
            if (player == winningPlayer) {
                PlayerStatEditor.addWon(player);
            } else {
                PlayerStatEditor.addLost(player);
            }
            sendBaseComponent(new ComponentBuilder(winningPlayer.getName() + " has won!").create(),
                    player);
        }}));
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
        joinActions.add(player -> JoinOneInTheQuiver(player, false));
    }

    private void JoinOneInTheQuiver(Player player, boolean onStart){
        if(!onStart && gameState == State.QUEUED) {
            sendBaseComponent(
                    new ComponentBuilder("You joined the game.").color(Style.INFO).create(),
                    player);
            return;
        }

        NamedTextColor color = OITQplayers.getOrDefault(player.getUniqueId(), GenerateNewPlayer(player)).getChatColor();
        KitOutPlayer(player);
        player.setGameMode(GameMode.SURVIVAL);
        TeamHandler.spawn(player, spawns);

        PVPPlugin.getInstance().sendMessage(
                String.format("<%s>%s has joined the game!</%s>",
                        color,
                        player.getName(),
                        color));
    }

    private PlayerTeam GenerateNewPlayer(Player player){
        PlayerTeam playerTeam = new PlayerTeam();
        playerTeam.setChatColor((NamedTextColor)NamedTextColor.NAMES.values().toArray()[OITQplayers.size() % 16]);
        playerTeam.setPlayerName(player.getName());
        OITQplayers.put(player.getUniqueId(), playerTeam);
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
        playerInventory.forEach(KitEditor::setUnbreaking);
    }

    public Boolean trySendMessage(Player player, Function<List<TagResolver>, Component> messageBuilder){
        if(!players.contains(player))
            return false;
        PlayerTeam team = OITQplayers.get(player.getUniqueId());
        if(team != null){
            PVPPlugin.getInstance().sendMessage(messageBuilder.apply(
                    List.of(Placeholder.parsed("prefix", team.getChatColor().examinableName()),
                            Placeholder.styling("color", team.getChatColor()))));
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

    private class OITQListener extends GamemodeListener{
        public OITQListener(){
            initOnPlayerDeathActions();
        }

        @Override
        protected void initOnPlayerDeathActions() {
            onPlayerDeathActions.add(e -> {
                Player player = e.getEntity();
                Player killer = player.getKiller();
                if(killer == null)
                    return;
                killer.getInventory().addItem(new ItemStack(Material.ARROW, 1));
                PlayerTeam killerTeam = OITQplayers.get(killer.getUniqueId());
                killerTeam.addKill();
                if(killerTeam.getKills() == scoreGoal){
                    winningPlayer = killer;
                    end(false);
                    return;
                }
                ScoreboardEditor.UpdateOneInTheQuiver(scoreboard, killerTeam);
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
            if(!(e.getEntity() instanceof Player player))
                return;
            if(!(e.getProjectile() instanceof Arrow arrow))
                return;
            if(!(players.contains(player)))
                return;
            arrow.setDamage(100);
        }
    }

    public static class PlayerTeam {
        int kills = 0;
        NamedTextColor chatColor;
        String playerName;

        public void addKill(){
            kills++;
        }
        public void setChatColor(NamedTextColor chatColor){
            this.chatColor = chatColor;
        }
        public NamedTextColor getChatColor(){return chatColor;}
        public void setPlayerName(String playerName) { this.playerName = playerName;}
        public String getPlayerName(){return playerName;}
        public int getKills() {return kills;}
    }
}
